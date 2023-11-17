package com.example.reciclerviewconretrofit.domain

import com.example.reciclerviewconretrofit.data.model.CustomerResponse
import java.time.LocalDate
import java.time.LocalDateTime

data class Customer(
    val id:Int,
    var first_name:String,
    val last_name:String,
    val phone:String?,
    val email:String?,
    val date_of_birth: LocalDate,
    var isSelected : Boolean = false,

    ) {

}