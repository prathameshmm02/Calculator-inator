package com.inator.calculator.Model

import retrofit2.Call
import retrofit2.http.GET

interface JsonPlaceholderApi {
    @get:GET("v4/latest/USD")
    val currency: Call<Currency?>?
}