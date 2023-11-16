package com.example.reciclerviewconretrofit.data.model

import com.example.reciclerviewconretrofit.domain.Order
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class OrderResponse (
    @SerializedName("order_id")
    val order_id:Int,
    @SerializedName("customer_id")
    val customer_id:Int,
    @SerializedName("order_date")
    val order_date:LocalDateTime,
    @SerializedName("table_id")
    val  table_id:Int

)
fun OrderResponse.toOrder(): Order =Order(order_id,customer_id,order_date,table_id)