package com.example.reciclerviewconretrofit.data.sources.remote

import com.example.reciclerviewconretrofit.data.model.OrderResponse
import com.example.reciclerviewconretrofit.domain.Order
import com.example.recyclerviewenhanced.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderService {
    @GET(Constants.ORDERS)
    suspend fun getOrders(): Response<List<OrderResponse>>
    @POST(Constants.ORDERS_CALL)
    suspend fun addOrder(@Body order: Order):Response<OrderResponse>
    @DELETE(Constants.ORDERS_POR_ID)
    suspend fun deleteOrder(@Path(Constants.ID)id:Int):Response<Int>
}