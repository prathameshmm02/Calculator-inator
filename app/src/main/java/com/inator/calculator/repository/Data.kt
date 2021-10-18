package com.inator.calculator.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.inator.calculator.model.Currency
import com.inator.calculator.model.JsonPlaceholderApi
import com.inator.calculator.model.Rate
import com.inator.calculator.util.CurrencyDeserializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class Data(context: Context) {

    companion object {
        private var instance: Data? = null

        fun getInstance(context: Context): Data {
            if (instance == null) {
                synchronized(Data::class) {
                    instance = Data(context)
                }
            }
            return instance!!
        }
    }

    private val preferenceRates: SharedPreferences =
        context.getSharedPreferences("Exchange-Rates", MODE_PRIVATE)

    private var isFetching: MutableLiveData<Boolean> = MutableLiveData(false)

    private fun saveExchangeRates(currency: Currency) {
        preferenceRates.apply {
            val editor = edit()
            editor.clear()
            editor.putString("_base", currency.base)
            editor.putString(
                "_date",
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currency.date!!)
            )
            currency.rates?.forEach {
                editor.putFloat(it.code, it.value)
            }
            editor.apply()
        }
    }

    fun getIsFetching(): LiveData<Boolean> {
        return isFetching
    }

    fun fetchExchangeRates() {

        isFetching.postValue(true)
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Currency::class.java, CurrencyDeserializer())
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangerate.host")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi::class.java)

        val call = jsonPlaceholderApi.currency

        call.enqueue(object : Callback<Currency?> {
            override fun onResponse(call: Call<Currency?>, response: Response<Currency?>) {
                saveExchangeRates(response.body()!!)
                isFetching.postValue(false)
            }

            override fun onFailure(call: Call<Currency?>, t: Throwable) {
                isFetching.postValue(false)
            }
        })
    }

    fun getExchangeRatesFromPreferences(): Currency? {

        return if (preferenceRates.getString(
                "_base",
                null
            ) == null || preferenceRates.getString("_date", null) == null
        )
            null
        else
            Currency(
                true,
                preferenceRates.getString("_base", null)!!,
                SimpleDateFormat("yyyy-MM-DD", Locale.getDefault()).parse(
                    preferenceRates.getString(
                        "_date",
                        null
                    )!!
                ),
                preferenceRates.all.entries
                    .filter { !it.key.startsWith("_") }
                    .sortedBy { it.key }
                    .map { Rate(it.key!!, (it.value as Float)) }
                    .toList()
            )
    }

    fun setTheme(theme: String) {
        AppCompatDelegate.setDefaultNightMode(
            when (theme) {
                "0" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                "1" -> AppCompatDelegate.MODE_NIGHT_NO
                else -> AppCompatDelegate.MODE_NIGHT_YES
            }
        )
    }

}