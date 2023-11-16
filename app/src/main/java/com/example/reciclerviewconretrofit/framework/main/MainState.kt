package com.example.reciclerviewconretrofit.framework.main

import com.example.reciclerviewconretrofit.domain.Customer

data class MainState (
    val customers:List<Customer>,
    val error:String?=null
)