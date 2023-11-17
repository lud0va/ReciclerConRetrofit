package com.example.reciclerviewconretrofit.data.sources.remote

import com.example.reciclerviewconretrofit.data.model.CustomerResponse
import com.example.reciclerviewconretrofit.domain.Customer
import com.example.recyclerviewenhanced.utils.Constants
import retrofit2.Response
import retrofit2.http.DELETE

import retrofit2.http.GET

interface CustomerService {


    @GET("customers")
    suspend fun getCustomers(): Response<List<CustomerResponse>>

    @GET("")
    suspend fun getCustomer():Response<Customer>
    @DELETE("")
    suspend fun deleteCustomer():Response<Int>

}