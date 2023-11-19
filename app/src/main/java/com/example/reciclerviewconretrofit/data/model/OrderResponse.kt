package com.example.reciclerviewconretrofit.data.model

import com.example.reciclerviewconretrofit.domain.Order
import com.example.recyclerviewenhanced.utils.Constants
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class OrderResponse (
    @SerializedName(Constants.ORDER_ID)
    val order_id:Int,
    @SerializedName(Constants.CUSTOMER_ID)
    val customer_id:Int,
    @SerializedName(Constants.ORDER_DATE)
    val order_date:LocalDateTime,
    @SerializedName(Constants.TABLE_ID)
    val  table_id:Int

)
fun OrderResponse.toOrder(): Order =Order(order_id,customer_id,order_date,table_id)