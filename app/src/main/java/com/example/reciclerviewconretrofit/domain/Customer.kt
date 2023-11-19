package com.example.reciclerviewconretrofit.domain


import java.time.LocalDate


data class Customer(
    val id:Int,
    var firstName:String,
    val lastName:String,
    val phone:String?,
    val email:String?,
    val dateOfBirth: LocalDate,
    var isSelected : Boolean = false,

    ) {

}