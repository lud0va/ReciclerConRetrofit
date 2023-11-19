package com.example.reciclerviewconretrofit.use_cases.juegos_usecases

import com.example.reciclerviewconretrofit.data.repositories.CustomerRepository
import com.example.reciclerviewconretrofit.data.repositories.OrderRepository
import javax.inject.Inject

class DeleteOrderUseCase   @Inject constructor(val orderRepository: OrderRepository) {
}