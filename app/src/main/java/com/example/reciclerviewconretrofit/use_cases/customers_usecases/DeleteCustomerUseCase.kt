package com.example.reciclerviewconretrofit.use_cases.customers_usecases

import com.example.reciclerviewconretrofit.data.repositories.CustomerRepository
import javax.inject.Inject

class DeleteCustomerUseCase @Inject constructor(private val customerRepository: CustomerRepository)  {

    suspend operator fun invoke(id:Int)=customerRepository.deleteCusto(id)
}