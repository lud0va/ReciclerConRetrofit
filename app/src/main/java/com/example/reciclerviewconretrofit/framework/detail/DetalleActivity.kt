package com.example.reciclerviewconretrofit.framework.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.reciclerviewconretrofit.domain.Customer
import com.example.reciclerviewconretrofit.domain.Order
import com.example.reciclerviewconretrofit.framework.main.CustomerAdapter
import com.example.reciclerviewconretrofit.framework.main.MainEvent
import com.example.recyclerviewenhanced.R
import com.example.recyclerviewenhanced.databinding.DetalleMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DetalleActivity : AppCompatActivity() {
    private lateinit var binding: DetalleMainBinding
    private lateinit var ordersAdapter: DetalleAdapter

    private var primeraVez: Boolean = false

    private var anteriorState: DetalleState? = null
    private val viewModel: DetalleViewModel by viewModels()


    private val callback by lazy {
        configContextBar()
    }

    private var actionMode: ActionMode? = null


    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetalleMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.extras?.let {
           viewModel.cargarCustomer(it.getInt("id"))

        }
     observarViewModel()
        ordersAdapter = DetalleAdapter(this,
            object : DetalleAdapter.OrderActions{
                override fun onDelete(order:Order) = viewModel.handleEvent(DetalleEvent.DeleteOrder(order))

                override fun onStartSelectMode(order:Order) {
                    viewModel.handleEvent(DetalleEvent.StartSelectMode)
                    viewModel.handleEvent(DetalleEvent.SeleccionaOrder(order))
                }

                override fun itemHasClicked(order:Order) {

                    viewModel.handleEvent(DetalleEvent.SeleccionaOrder(order))
                }



            })
        binding.rvOrders.adapter = ordersAdapter

        val touchHelper = ItemTouchHelper(ordersAdapter.swipeGesture)
        touchHelper.attachToRecyclerView(binding.rvOrders)

        viewModel.uiState.observe(this) { state ->
            if (state.orders != anteriorState?.orders
                && state.orders.isNotEmpty())
                ordersAdapter.submitList(state.orders)

            if (state.orderSeleccionadas != anteriorState?.orderSeleccionadas) {
                ordersAdapter.setSelectedItems(state.orderSeleccionadas)
                actionMode?.title =
                    "${state.orderSeleccionadas.size} selected"
            }

            if (state.selecMode != anteriorState?.selecMode) {
                if (state.selecMode) {
                    ordersAdapter.startSelectMode()
                    if (primeraVez) {
                        startSupportActionMode(callback)?.let {
                            actionMode = it;
                        }
                        primeraVez = false
                    }

                } else {
                    ordersAdapter.resetSelectMode()
                    primeraVez = true
                    actionMode?.finish()
                }
            }
            state.error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.handleEvent(DetalleEvent.ErrorVisto)
            }

            anteriorState = state
        }

        val context = this
        lifecycleScope.launch {
            viewModel.sharedFlow.collect(){ error->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }

        }

        viewModel.handleEvent(DetalleEvent.GetOrders)
        configAppBar();

    }

    private fun observarViewModel() {
        viewModel.uiState.observe(this@DetalleActivity) { state ->
            state.error?.let { error ->
                Timber.e(error)
                viewModel.errorMostrado()
            }
            val cust=state.customer
            binding.firstNameText.setText(cust?.first_name)
            binding.lastNameText.setText(cust?.last_name)
            binding.emailText.setText(cust?.email)
            binding.dateText.setText(cust?.date_of_birth.toString())
            binding.phoneText.setText(cust?.phone)

        }
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
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
                        // Handle share icon press
                        true
                    }

                    R.id.search -> {
                        // Handle delete icon press
                        true
                    }

                    R.id.more -> {
                        viewModel.handleEvent(DetalleEvent.DeleteOrdersSeleccionadas())
                        true
                    }

                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                viewModel.handleEvent(DetalleEvent.ResetSelectMode)

            }

        }

    private fun configAppBar() {
        binding.topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
        }


        val actionSearch = binding.topAppBar.menu.findItem(R.id.search).actionView as SearchView

        actionSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                newText?.let { filtro ->
                    viewModel.handleEvent(DetalleEvent.GetOrdersFiltrados(filtro))
                }

                return false
            }


        })

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete -> {
                    // Handle favorite icon press
                    true
                }

                R.id.search -> {
                    // Handle search icon press
                    true
                }

                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    true
                }

                else -> false
            }
        }
    }
}

