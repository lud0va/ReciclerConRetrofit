package com.example.reciclerviewconretrofit.use_cases.juegos_usecases

import com.example.reciclerviewconretrofit.data.repositories.CustomerRepository
import javax.inject.Inject

class GetJuegoUseCase  @Inject constructor(val juegorepo: CustomerRepository) {
}