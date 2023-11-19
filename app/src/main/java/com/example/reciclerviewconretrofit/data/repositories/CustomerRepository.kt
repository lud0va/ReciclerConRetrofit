package com.example.reciclerviewconretrofit.data.repositories


import com.example.reciclerviewconretrofit.data.model.toCustomer
import com.example.reciclerviewconretrofit.data.sources.remote.CustomerService
import com.example.reciclerviewconretrofit.domain.Customer
import com.example.reciclerviewconretrofit.utils.NetworkResultt

import dagger.hilt.android.scopes.ActivityRetainedScoped

import javax.inject.Inject

@ActivityRetainedScoped
class CustomerRepository @Inject constructor(
    private val customerService: CustomerService
) {

    suspend fun getAllCustomers(): NetworkResultt<List<Customer>> {
        try {
            val response = customerService.getCustomers()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    val customers = it.map { customerResponse ->
                        customerResponse.toCustomer()
                    }
                    return NetworkResultt.Success(customers)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    suspend fun getCustomer(id: Int): NetworkResultt<Customer> {
        try {
            val response = customerService.getCustomer(id)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    val customer= it.toCustomer()
                    return NetworkResultt.Success(customer)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }
}
