package com.example.reciclerviewconretrofit.framework.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reciclerviewconretrofit.domain.Order
import com.example.reciclerviewconretrofit.use_cases.customers_usecases.GetCustomerUseCase
import com.example.reciclerviewconretrofit.use_cases.juegos_usecases.AddOrderUseCase
import com.example.reciclerviewconretrofit.use_cases.juegos_usecases.DeleteOrderUseCase
import com.example.reciclerviewconretrofit.use_cases.juegos_usecases.GetAllOrdersUseCase
import com.example.reciclerviewconretrofit.utils.NetworkResultt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DetalleViewModel @Inject constructor(
    private val getCustomerUseCase: GetCustomerUseCase,
    private val getOrderUseCase: GetAllOrdersUseCase,
    private val deleteOrderUseCase: DeleteOrderUseCase,
    private val addOrderUseCase: AddOrderUseCase
) :
    ViewModel() {


    private val _error = MutableLiveData<String>()

    private val _uiState = MutableLiveData(DetalleState())
    val uiState: LiveData<DetalleState> get() = _uiState

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()


    private var listaOrders = mutableListOf<Order>()


    private var selectedPersonas = mutableListOf<Order>()
    fun errorMostrado() {
        _uiState.value = _uiState.value?.copy(error = null)
    }

    fun handleEvent(event: DetalleEvent) {
        when (event) {
            DetalleEvent.GetOrders -> {
                getOrders()

            }

            is DetalleEvent.InsertOrder -> {
                //insertPersonaWithCosas(event.persona!!)
                getOrders()
            }

            DetalleEvent.ErrorVisto -> _uiState.value = _uiState.value?.copy(error = null)


            is DetalleEvent.SeleccionaOrder -> seleccionaOrder(event.order)
            is DetalleEvent.DeleteOrder -> {

                deleteOrder(event.order)
            }

            DetalleEvent.ResetSelectMode -> resetSelectMode()

            DetalleEvent.StartSelectMode -> _uiState.value = _uiState.value?.copy(selecMode = true)
        }
    }


    private fun resetSelectMode() {
        selectedPersonas.clear()

        _uiState.value =
            _uiState.value?.copy(selecMode = false, orderSeleccionadas = selectedPersonas)

    }

    private fun deleteOrder(order: Order) {
        runBlocking {
            deleteOrderUseCase.invoke(order.order_id)
        }
        deleteOrder(listOf(order))

    }

    fun cargarCustomer(id: Int) {
        runBlocking {
            _uiState.value = _uiState.value?.copy(customer = getCustomerUseCase.invoke(id).data)
        }


    }

    private fun deleteOrder(personas: List<Order>) {

        viewModelScope.launch {
//            _sharedFlow.emit("error")
            listaOrders.removeAll(personas)
            selectedPersonas.removeAll(personas)
            _uiState.value =
                _uiState.value?.copy(orderSeleccionadas = selectedPersonas.toList())
            getOrders()
        }

    }

    private fun getOrders() {

        viewModelScope.launch {


            when (val result = _uiState.value?.customer?.id?.let { getOrderUseCase.invoke(it) }) {
                is NetworkResultt.Error -> _error.value = result.message ?: ""
                is NetworkResultt.Loading -> TODO()
                is NetworkResultt.Success -> listaOrders =
                    result.data as MutableList<Order>


                else -> {
                    _error
                }
            }





            _uiState.value = _uiState.value?.copy(orders = listaOrders.toList())
//            _personas.value = getPersonas.invoke()

        }

    }

    fun addOrder() {

        _uiState.value?.customer?.id?.let { customerId ->
            val cust: Int = customerId
            runBlocking {
                addOrderUseCase.invoke(Order(0, cust, LocalDateTime.now(), 3, false))
            }

        }
        getOrders()
    }

    private fun seleccionaOrder(order: Order) {

        if (isSelected(order)) {
            selectedPersonas.remove(order)

        } else {
            selectedPersonas.add(order)
        }
        _uiState.value = _uiState.value?.copy(orderSeleccionadas = selectedPersonas)

    }

    private fun isSelected(order: Order): Boolean {
        return selectedPersonas.contains(order)
    }


}