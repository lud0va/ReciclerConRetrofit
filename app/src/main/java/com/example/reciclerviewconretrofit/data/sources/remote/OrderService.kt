package com.example.reciclerviewconretrofit.data.sources.remote

import com.example.reciclerviewconretrofit.data.model.OrderResponse
import retrofit2.Response
import retrofit2.http.GET

interface OrderService {
    @GET("orders")
    suspend fun getOrders(): Response<List<OrderResponse>>
}