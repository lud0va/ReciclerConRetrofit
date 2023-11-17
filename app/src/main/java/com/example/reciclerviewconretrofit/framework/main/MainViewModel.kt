package com.example.reciclerviewconretrofit.framework.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reciclerviewconretrofit.domain.Customer
import com.example.reciclerviewconretrofit.use_cases.customers_usecases.GetAllCustomersUseCase
import com.example.reciclerviewconretrofit.utils.NetworkResultt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val getAllCustomers: GetAllCustomersUseCase) :
    ViewModel() {
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _uiState = MutableLiveData(MainState())
    val uiState: LiveData<MainState> get() = _uiState

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    private var listaPersonas = mutableListOf<Customer>()


    private var selectedPersonas = mutableListOf<Customer>()

    fun handleEvent(event: MainEvent) {
        when (event) {
            MainEvent.GetCustomers -> {
                getCustomers()

            }
            is MainEvent.InsertCustomer -> {
                //insertPersonaWithCosas(event.persona!!)
                getCustomers()
            }
            MainEvent.ErrorVisto -> _uiState.value = _uiState.value?.copy(error = null)


            is MainEvent.DeleteCustomersSeleccionadas -> {
                _uiState.value?.let {
                    deleteCustomers(it.customersSeleccionados)
                    resetSelectMode()
                }
            }
            is MainEvent.SeleccionaCustomer -> seleccionaCustomer(event.customer)
            is MainEvent.GetCustomersFiltrados -> getPersonas(event.filtro)
            is MainEvent.DeleteCustomer -> {
                deleteCustomers(event.custo)
            }

            MainEvent.ResetSelectMode -> resetSelectMode()

            MainEvent.StartSelectMode -> _uiState.value = _uiState.value?.copy(selecMode = true)
            else -> {}
        }
    }
    private fun resetSelectMode()
    {
        selectedPersonas.clear()

        _uiState.value = _uiState.value?.copy(selecMode = false, customersSeleccionados = selectedPersonas)

    }
    private fun deleteCustomers(persona: Customer) {

        deleteCustomers(listOf(persona))

    }
    private fun deleteCustomers(personas: List<Customer>) {

        viewModelScope.launch {
//            _sharedFlow.emit("error")
            listaPersonas.removeAll(personas)
            selectedPersonas.removeAll(personas)
            _uiState.value = _uiState.value?.copy(customersSeleccionados = selectedPersonas.toList())
            getCustomers()
        }

    }
    private fun getCustomers() {

        viewModelScope.launch {

            var result = getAllCustomers.invoke()


           when (result) {
                is NetworkResultt.Error -> _error.value = result.message ?: ""
                is NetworkResultt.Loading -> TODO()
                is NetworkResultt.Success ->listaPersonas= result.data as MutableList<Customer>
            }


            result = getAllCustomers.invoke()




            _uiState.value = _uiState.value?.copy(customers = listaPersonas.toList())
//            _personas.value = getPersonas.invoke()

        }

    }
    private fun getPersonas(filtro: String) {

        viewModelScope.launch {

            _uiState.value =  _uiState.value?.copy (
                customers = listaPersonas.filter { it.first_name.startsWith(filtro) }.toList())
//            _personas.value = getPersonas.invoke()

        }

    }

    private fun seleccionaCustomer(customer: Customer) {

        if (isSelected(customer)) {
            selectedPersonas.remove(customer)

        }
        else {
            selectedPersonas.add(customer)
        }
        _uiState.value = _uiState.value?.copy(customersSeleccionados = selectedPersonas)

    }

    private fun isSelected(customer: Customer): Boolean {
        return selectedPersonas.contains(customer)
    }


}