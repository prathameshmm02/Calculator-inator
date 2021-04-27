package com.inator.calculator.repository

import android.content.Context
import android.content.SharedPreferences
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

    private val appPrefs: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    fun saveMeasureToPrefs(measure: String) {
        appPrefs.apply {
            val editor = edit()
            editor.putString("selected_measure", measure)
            editor.apply()
        }
    }

    fun saveCurrencySpinner1Prefs(rate: Rate) {
        appPrefs.apply {
            val editor = edit()
            editor.putString("currency_spinner_1", rate.code)
            editor.apply()
        }
    }

    fun saveCurrencySpinner2Prefs(rate: Rate) {
        appPrefs.apply {
            val editor = edit()
            editor.putString("currency_spinner_2", rate.code)
            editor.apply()
        }
    }

    fun saveConverterSpinner1Prefs(position: Int) {
        appPrefs.apply {
            val editor = edit()
            editor.putInt("converter_spinner_1", position)
            editor.apply()
        }
    }

    fun saveConverterSpinner2Prefs(position: Int) {
        appPrefs.apply {
            val editor = edit()
            editor.putInt("converter_spinner_2", position)
            editor.apply()
        }
    }

    fun getMeasurePrefs(): String? {
        return appPrefs.getString("selected_measure", "Length")
    }

    fun getConverterSpinner1(): Int {
        return appPrefs.getInt("converter_spinner_1", 0)
    }

    fun getConverterSpinner2(): Int {
        return appPrefs.getInt("converter_spinner_2", 1)
    }

    fun getCurrencySpinner1(): String? {
        return appPrefs.getString("currency_spinner_1", "AED")
    }

    fun getCurrencySpinner2(): String? {
        return appPrefs.getString("currency_spinner_2", "AFN")
    }
}