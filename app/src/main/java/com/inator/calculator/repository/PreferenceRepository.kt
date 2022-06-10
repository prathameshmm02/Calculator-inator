package com.inator.calculator.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.inator.calculator.model.Rate

class PreferenceRepository(context: Context) {

    companion object {
        private var instance: PreferenceRepository? = null

        fun getInstance(context: Context): PreferenceRepository {
            if (instance == null) {
                synchronized(PreferenceRepository::class) {
                    instance = PreferenceRepository(context)
                }
            }
            return instance!!
        }
    }

    private val appPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveMeasureToPrefs(measure: String) {
        appPrefs.apply {
            val editor = edit()
            editor.putString(MEASURE, measure)
            editor.apply()
        }
    }

    fun saveCurrencySpinner1Prefs(rate: Rate) {
        appPrefs.apply {
            val editor = edit()
            editor.putString(CURRENCY_SPINNER_1, rate.code)
            editor.apply()
        }
    }

    fun saveCurrencySpinner2Prefs(rate: Rate) {
        appPrefs.apply {
            val editor = edit()
            editor.putString(CURRENCY_SPINNER_2, rate.code)
            editor.apply()
        }
    }

    fun saveConverterSpinner1Prefs(position: Int) {
        appPrefs.apply {
            val editor = edit()
            editor.putInt(CONVERTER_SPINNER_1, position)
            editor.apply()
        }
    }

    fun saveConverterSpinner2Prefs(position: Int) {
        appPrefs.apply {
            val editor = edit()
            editor.putInt(CONVERTER_SPINNER_2, position)
            editor.apply()
        }
    }

    fun getMeasurePrefs(): String? {
        return appPrefs.getString(MEASURE, "Length")
    }

    fun getConverterSpinner1(): Int {
        return appPrefs.getInt(CONVERTER_SPINNER_1, 0)
    }

    fun getConverterSpinner2(): Int {
        return appPrefs.getInt(CONVERTER_SPINNER_2, 1)
    }

    fun clearSpinnerSelections() {
        appPrefs.edit {
            remove(CONVERTER_SPINNER_1)
            remove(CONVERTER_SPINNER_2)
        }
    }

    fun getCurrencySpinner1(): String? {
        return appPrefs.getString(CURRENCY_SPINNER_1, "AED")
    }

    fun getCurrencySpinner2(): String? {
        return appPrefs.getString(CURRENCY_SPINNER_2, "AFN")
    }
}