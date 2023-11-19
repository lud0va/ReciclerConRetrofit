package com.example.reciclerviewconretrofit.use_cases.juegos_usecases

import com.example.reciclerviewconretrofit.data.repositories.OrderRepository
import javax.inject.Inject

class DeleteOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {

    suspend fun invoke(id: Int) = orderRepository.deleteOrd(id)
}