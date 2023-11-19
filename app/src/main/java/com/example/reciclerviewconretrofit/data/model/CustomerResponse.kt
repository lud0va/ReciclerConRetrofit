package com.example.reciclerviewconretrofit.data.model

import com.example.reciclerviewconretrofit.domain.Customer
import com.example.recyclerviewenhanced.utils.Constants
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class CustomerResponse(
    @SerializedName(Constants.ID)
    val id: Int,
    @SerializedName(Constants.FIRST_NAME)
    val first_name: String,
    @SerializedName(Constants.LAST_NAME)
    val last_name: String,
    @SerializedName(Constants.EMAIL)
    val email: String,
    @SerializedName(Constants.PHONE)
    val phone:String,
    @SerializedName(Constants.DATE_OF_BIRTH)
    val date_of_birth:LocalDate
)

public fun CustomerResponse.toCustomer(): Customer = Customer(id, first_name, last_name, email, phone, date_of_birth)