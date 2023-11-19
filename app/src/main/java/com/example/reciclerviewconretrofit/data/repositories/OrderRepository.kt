package com.example.reciclerviewconretrofit.data.repositories

import com.example.reciclerviewconretrofit.data.model.toOrder
import com.example.reciclerviewconretrofit.data.sources.remote.OrderService
import com.example.reciclerviewconretrofit.domain.Order
import com.example.reciclerviewconretrofit.utils.NetworkResultt
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
@ActivityRetainedScoped
class OrderRepository @Inject constructor(
    private  val orderService: OrderService){
    suspend fun getAllOrders(id: Int): NetworkResultt<List<Order>> {
        try {
            val response =orderService.getOrders()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    var customers = it.map { orderResponse ->
                        orderResponse.toOrder()
                    }
                   customers= customers.filter { it.customer_id==id }
                    return NetworkResultt.Success(customers)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }


}