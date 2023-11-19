package com.example.reciclerviewconretrofit.use_cases.juegos_usecases

import com.example.reciclerviewconretrofit.data.repositories.OrderRepository
import com.example.reciclerviewconretrofit.domain.Order
import javax.inject.Inject

class AddOrderUseCase  @Inject constructor(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(order: Order)=orderRepository.addOrder(order)
}
