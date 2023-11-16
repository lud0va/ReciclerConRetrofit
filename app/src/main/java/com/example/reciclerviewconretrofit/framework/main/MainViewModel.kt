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


    private val _uiState = MutableLiveData<MainState>()
    private val uiState: LiveData<MainState> get() = _uiState

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    private var selectedItem = mutableListOf<Customer>()

    fun handleEvent(event: MainEvent) {
        when (event) {
            MainEvent.GetCustomers -> {
                getAllCustomers
            }
            is MainEvent.InsertPersona -> {
                //insertPersonaWithCosas(event.persona!!)
                getPersonas()
            }
            MainEvent.ErrorVisto -> _uiState.value = _uiState.value?.copy(error = null)
            is MainEvent.GetPersonaPorId -> {
            }

            is MainEvent.DeletePersonasSeleccionadas -> {
                _uiState.value?.let {
                    deletePersona(it.personasSeleccionadas)
                    resetSelectMode()
                }
            }
            is MainEvent.SeleccionaPersona -> seleccionaPersona(event.persona)
            is MainEvent.GetCustomersFiltrados -> getPersonas(event.filtro)
            is MainEvent.DeletePersona -> {
                deletePersona(event.persona)
            }

            MainEvent.ResetSelectMode -> resetSelectMode()

            MainEvent.StartSelectMode -> _uiState.value = _uiState.value?.copy(selectMode = true)
        }
    }
    private fun resetSelectMode()
    {
        selectedPersonas.clear()
        _uiState.value = _uiState.value?.copy(selectMode = false, personasSeleccionadas = selectedPersonas)

    }

    private fun getCustomers() {

        viewModelScope.launch {

            var result = getAllCustomers


            when (result) {
                is NetworkResultt.Error -> _error.value = result.message ?: ""
                is NetworkResultt.Loading -> TODO()
                is NetworkResultt.Success -> listaPersonas[0].nombre = result.data?.nombre ?: ""
            }


            result = dogRepository.getDog()

            when (result) {
                is NetworkResultt.Error -> _error.value = result.message ?: ""
                is NetworkResultt.Loading -> TODO()
                is NetworkResultt.Success -> listaPersonas[1].nombre = result.data?.nombre ?: ""
            }


            _uiState.value = _uiState.value?.copy(personas = listaPersonas.toList())
//            _personas.value = getPersonas.invoke()

        }

    }


}