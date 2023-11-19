package com.example.reciclerviewconretrofit.data.sources.remote

import com.example.reciclerviewconretrofit.data.model.CustomerResponse
import com.example.reciclerviewconretrofit.domain.Customer
import com.example.recyclerviewenhanced.utils.Constants
import retrofit2.Response
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.Path

interface CustomerService {


    @GET("customers")
    suspend fun getCustomers(): Response<List<CustomerResponse>>

    @GET("customers/{id}")

    suspend fun getCustomer(@Path("id") id:Int):Response<CustomerResponse>
    @DELETE("")
    suspend fun deleteCustomer():Response<Int>

}