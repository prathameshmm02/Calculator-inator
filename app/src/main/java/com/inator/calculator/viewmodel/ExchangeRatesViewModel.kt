package com.inator.calculator.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inator.calculator.Model.Currency
import com.inator.calculator.repository.Data
import com.inator.calculator.repository.ExchangeRatesRepository

class ExchangeRatesViewModel(application: Application) : AndroidViewModel(application) {
    private val currencyLiveData: MutableLiveData<Currency> = MutableLiveData<Currency>()
    private val repository: ExchangeRatesRepository = ExchangeRatesRepository()
    val success: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val rates: MutableLiveData<Currency> = MutableLiveData<Currency>()

    fun getExchangeRates(context: Context): LiveData<Currency>{
        currencyLiveData.value = repository.getSavedExchangeRates(context)
        return currencyLiveData
    }



}