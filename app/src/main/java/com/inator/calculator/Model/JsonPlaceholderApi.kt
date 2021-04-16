package com.inator.calculator.Model

import retrofit2.Call
import retrofit2.http.GET

interface JsonPlaceholderApi {
    @get:GET("latest")
    val currency: Call<Currency?>?
}