package com.example.reciclerviewconretrofit.framework.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.reciclerviewconretrofit.domain.Customer
import com.example.reciclerviewconretrofit.framework.detail.DetalleActivity
import com.example.recyclerviewenhanced.R
import com.example.recyclerviewenhanced.databinding.ActivityMainBinding
import com.example.recyclerviewenhanced.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
   private lateinit var binding: ActivityMainBinding

   private var primeraVez : Boolean = false

   private var anteriorState: MainState? = null

   private lateinit var personasAdapter: CustomerAdapter


   private val viewModel: MainViewModel by viewModels()


   private val callback by lazy {
      configContextBar()
   }

   private var actionMode: ActionMode? = null



   private fun cambiarPantalla(id: Int) {
      val intent = Intent(this@MainActivity, DetalleActivity::class.java)
      intent.putExtra(Constants.ID, id)
      startActivity(intent)
   }
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      primeraVez = true
      binding = ActivityMainBinding.inflate(layoutInflater)
      setContentView(binding.root)
      personasAdapter = CustomerAdapter(this,
         object : CustomerAdapter.CustomerActions{
            override fun onDelete(customer: Customer) = viewModel.handleEvent(MainEvent.DeleteCustomer(customer))

            override fun onStartSelectMode(customer: Customer) {
               viewModel.handleEvent(MainEvent.StartSelectMode)
               viewModel.handleEvent(MainEvent.SeleccionaCustomer(customer))
            }

            override fun itemHasClicked(customer: Customer) {

               viewModel.handleEvent(MainEvent.SeleccionaCustomer(customer))
            }



         },::cambiarPantalla)
      binding.rvPersonas.adapter = personasAdapter

      val touchHelper = ItemTouchHelper(personasAdapter.swipeGesture)
      touchHelper.attachToRecyclerView(binding.rvPersonas)


      viewModel.uiState.observe(this) { state ->
         if (state.customers != anteriorState?.customers
            && state.customers.isNotEmpty())
            personasAdapter.submitList(state.customers)

         if (state.customersSeleccionados != anteriorState?.customersSeleccionados) {
            personasAdapter.setSelectedItems(state.customersSeleccionados)
            actionMode?.title =
               "${state.customersSeleccionados.size}"
         }

         if (state.selecMode != anteriorState?.selecMode) {
            if (state.selecMode) {
               personasAdapter.startSelectMode()
               if (primeraVez) {
                  startSupportActionMode(callback)?.let {
                     actionMode = it
                  }
                  primeraVez = false
               }

            } else {
               personasAdapter.resetSelectMode()
               primeraVez = true
               actionMode?.finish()
            }
         }
         state.error?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            viewModel.handleEvent(MainEvent.ErrorVisto)
         }

         anteriorState = state
      }

      val context = this
      lifecycleScope.launch {
         viewModel.sharedFlow.collect { error->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
         }

      }

      viewModel.handleEvent(MainEvent.GetCustomers)
      configAppBar()

   }


   private fun configContextBar() =
      object : ActionMode.Callback {

         override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.context_bar, menu)
            return true
         }

         override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
         }

         override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {
               R.id.delete -> {
                   if (viewModel.uiState.value?.customersSeleccionados!=null){
                      viewModel.uiState.value?.customersSeleccionados?.let { viewModel.deleteCustomers(it) }
                   }
                  true
               }

               R.id.more -> {
                  viewModel.handleEvent(MainEvent.DeleteCustomersSeleccionadas())
                  true
               }
               else -> false
            }
         }

         override fun onDestroyActionMode(mode: ActionMode?) {
            viewModel.handleEvent(MainEvent.ResetSelectMode)

         }

      }

   private fun configAppBar() {
      binding.topAppBar.setNavigationOnClickListener {

      }


      val actionSearch = binding.topAppBar.menu.findItem(R.id.search).actionView as SearchView

      actionSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
         override fun onQueryTextSubmit(p0: String?): Boolean {
            return false
         }

         override fun onQueryTextChange(newText: String?): Boolean {

            newText?.let {filtro ->
               viewModel.handleEvent(MainEvent.GetCustomersFiltrados(filtro))
            }

            return false
         }


      })

      binding.topAppBar.setOnMenuItemClickListener { menuItem ->
         when (menuItem.itemId) {
            R.id.more -> {

               true
            }
            else -> false
         }
      }
   }
}