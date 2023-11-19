package com.example.reciclerviewconretrofit.domain

import java.time.LocalDateTime

data class Order(
    val order_id:Int,
    val customer_id:Int,
    val order_date:LocalDateTime,
    val table_id:Int,
    var isSelected : Boolean = false,
    )