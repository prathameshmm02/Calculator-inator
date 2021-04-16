package com.inator.calculator.repository

import android.content.Context
import com.inator.calculator.Model.Currency

class ExchangeRatesRepository {
    fun getSavedExchangeRates(context: Context): Currency? {
        return Data.getInstance(context).getExchangeRatesFromPreferences()
    }


}