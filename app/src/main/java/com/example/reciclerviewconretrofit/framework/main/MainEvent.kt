package com.example.reciclerviewconretrofit.framework.main

import com.example.reciclerviewconretrofit.domain.Customer


sealed class MainEvent {


    class DeleteCustomersSeleccionadas() : MainEvent()
    class DeleteCustomer(val custo:Customer) : MainEvent()
    class SeleccionaCustomer(val customer: Customer) : MainEvent()
    class InsertCustomer(val persona: Customer) : MainEvent()
    class GetCustomerPorId(val id: Int) : MainEvent()

    class GetCustomersFiltrados(val filtro: String) : MainEvent()
    object GetCustomers : MainEvent()
    object ErrorVisto : MainEvent()

    object StartSelectMode: MainEvent()
    object ResetSelectMode: MainEvent()
}
