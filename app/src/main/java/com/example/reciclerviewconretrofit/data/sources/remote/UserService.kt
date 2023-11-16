package com.example.reciclerviewconretrofit.data.sources.remote

import com.example.reciclerviewconretrofit.data.model.OrderResponse
import com.example.recyclerviewenhanced.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface UserService {
    @GET(Constants.RANDOM_URL)
    suspend fun getPost(): Response<OrderResponse>
}