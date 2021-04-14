package com.inator.calculator

import android.app.Application
import android.util.Log
import com.inator.calculator.Model.Currency
import com.inator.calculator.Model.JsonPlaceholderApi
import com.inator.calculator.Model.Rates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StartUp : Application() {
    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val jsonPlaceholderApi = retrofit.create(
            JsonPlaceholderApi::class.java
        )
        val call = jsonPlaceholderApi.currency
        call!!.enqueue(object : Callback<Currency?> {
            override fun onResponse(call: Call<Currency?>, response: Response<Currency?>) {
                val currencyData = response.body()
                Log.i("response", response.toString())
                assert(currencyData != null)
                currentRates = currencyData?.rates
            }

            override fun onFailure(call: Call<Currency?>, t: Throwable) {}
        })
    }

    companion object {
        var currentRates: Rates? = null
    }
}