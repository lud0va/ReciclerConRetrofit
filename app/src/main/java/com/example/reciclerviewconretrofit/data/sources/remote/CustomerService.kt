package com.example.reciclerviewconretrofit.data.sources.remote

import com.example.reciclerviewconretrofit.data.model.CustomerResponse
import com.example.recyclerviewenhanced.utils.Constants

import retrofit2.Response
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.Path

interface CustomerService {


    @GET(Constants.CUSTOMER)
    suspend fun getCustomers(): Response<List<CustomerResponse>>

    @GET(Constants.CUSTOMERS_POR_ID)

    suspend fun getCustomer(@Path(Constants.ID) id:Int):Response<CustomerResponse>
    @DELETE(Constants.CUSTOMERS_POR_ID)
    suspend fun deleteCustomer(@Path(Constants.ID) id:Int):Response<Int>

}