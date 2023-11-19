package com.example.reciclerviewconretrofit.framework.detail

import com.example.reciclerviewconretrofit.domain.Customer
import com.example.reciclerviewconretrofit.domain.Order

data class DetalleState(
    val customer: Customer?=null,
    val orders:List<Order> = emptyList(),
    val orderSeleccionadas:List<Order> = emptyList(),
    val selecMode:Boolean=false,
    val error: String? = null

)