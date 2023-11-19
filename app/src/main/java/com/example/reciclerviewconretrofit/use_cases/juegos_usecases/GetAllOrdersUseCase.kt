package com.example.reciclerviewconretrofit.use_cases.juegos_usecases

import com.example.reciclerviewconretrofit.data.repositories.OrderRepository
import javax.inject.Inject

class GetAllOrdersUseCase @Inject constructor(val orderRepository: OrderRepository) {

    suspend operator fun invoke(id:Int)=orderRepository.getAllOrders(id)
}