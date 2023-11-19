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
import com.example.reciclerviewconretrofit.domain.Order
import com.example.recyclerviewenhanced.R
import com.example.recyclerviewenhanced.databinding.DetalleMainBinding
import com.example.recyclerviewenhanced.utils.Constants
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetalleMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.extras?.let {
            viewModel.cargarCustomer(it.getInt(Constants.ID))

        }
        observarViewModel()
        ordersAdapter = DetalleAdapter(this,
            object : DetalleAdapter.OrderActions {
                override fun onDelete(order: Order) =
                    viewModel.handleEvent(DetalleEvent.DeleteOrder(order))

                override fun onStartSelectMode(order: Order) {
                    viewModel.handleEvent(DetalleEvent.StartSelectMode)
                    viewModel.handleEvent(DetalleEvent.SeleccionaOrder(order))
                }

                override fun itemHasClicked(order: Order) {

                    viewModel.handleEvent(DetalleEvent.SeleccionaOrder(order))
                }


            })
        binding.rvOrders.adapter = ordersAdapter

        val touchHelper = ItemTouchHelper(ordersAdapter.swipeGesture)
        touchHelper.attachToRecyclerView(binding.rvOrders)

        viewModel.uiState.observe(this) { state ->
            if (state.orders != anteriorState?.orders
                && state.orders.isNotEmpty()
            )
                ordersAdapter.submitList(state.orders)

            if (state.orderSeleccionadas != anteriorState?.orderSeleccionadas) {
                ordersAdapter.setSelectedItems(state.orderSeleccionadas)
                actionMode?.title =
                    "${state.orderSeleccionadas.size}"
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
            viewModel.sharedFlow.collect() { error ->
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
            val cust = state.customer
            binding.firstNameText.setText(cust?.firstName)
            binding.lastNameText.setText(cust?.lastName)
            binding.emailText.setText(cust?.email)
            binding.dateText.setText(cust?.dateOfBirth.toString())
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



                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                viewModel.handleEvent(DetalleEvent.ResetSelectMode)

            }

        }

    private fun configAppBar() {
        binding.topAppBar.setNavigationOnClickListener {

        }




        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add->{
                    viewModel.addOrder()
                    true
                }
                R.id.delete -> {

                    true
                }



                R.id.more -> {

                    true
                }

                else -> false
            }
        }
    }
}

