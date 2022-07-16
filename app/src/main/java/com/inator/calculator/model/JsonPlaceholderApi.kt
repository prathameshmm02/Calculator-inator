package com.inator.calculator.model

import retrofit2.Call
import retrofit2.http.GET

interface JsonPlaceholderApi {
    @GET("latest")
    fun getCurrency() : Call<Currency>
}