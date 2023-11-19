package com.example.reciclerviewconretrofit.use_cases.customers_usecases

import com.example.reciclerviewconretrofit.data.repositories.CustomerRepository
import javax.inject.Inject

class GetAllCustomersUseCase @Inject constructor(val customerRepository: CustomerRepository  ) {

    suspend operator fun invoke()=customerRepository.getAllCustomers()
}