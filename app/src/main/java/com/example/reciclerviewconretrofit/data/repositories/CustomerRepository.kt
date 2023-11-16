package com.example.reciclerviewconretrofit.data.repositories



import com.example.reciclerviewconretrofit.data.sources.remote.CustomerService
import com.example.reciclerviewconretrofit.domain.Customer
import com.example.reciclerviewconretrofit.utils.NetworkResultt
import com.example.recyclerviewenhanced.utils.Constants
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@ActivityRetainedScoped
class CustomerRepository @Inject constructor(
 private val customerService:CustomerService
) {
    class LocalDateAdapter {
        companion object {
            @ToJson
            fun toJson(value: LocalDate): String {
                return FORMATTER.format(value)
            }

            @FromJson
            fun fromJson(value: String): LocalDate {
                return LocalDate.from(FORMATTER.parse(value))
            }

            private val FORMATTER = DateTimeFormatter.ofPattern(Constants.YEAR_PARSE)
        }
    }

    companion object{
        private val lista = mutableListOf<Customer>()

    }
    suspend fun getCustomer() : NetworkResultt<List<Customer>>{
        try {
            val response = customerService.getCustomers()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResultt.Success(body)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }



}
