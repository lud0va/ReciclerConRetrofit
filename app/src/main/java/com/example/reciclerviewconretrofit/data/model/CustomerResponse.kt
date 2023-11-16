package com.example.reciclerviewconretrofit.data.model

import com.example.reciclerviewconretrofit.domain.Customer
import com.example.recyclerviewenhanced.utils.Constants
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class CustomerResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("first_name")
    val first_name: String,
    @SerializedName("last_name")
    val last_name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone:String,
    @SerializedName("date_of_birth")
    val date:LocalDate
)

fun CustomerResponse.toCustomer():Customer=Customer(id,first_name,last_name,email,phone,date)