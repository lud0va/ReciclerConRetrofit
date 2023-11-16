package com.example.reciclerviewconretrofit.framework.main

import com.example.reciclerviewconretrofit.domain.Customer

data class MainState (
    val customers:List<Customer> = emptyList(),
    val customersSeleccionados:List<Customer> = emptyList(),
    val selecMode:Boolean=false,
    val error:String?=null
)