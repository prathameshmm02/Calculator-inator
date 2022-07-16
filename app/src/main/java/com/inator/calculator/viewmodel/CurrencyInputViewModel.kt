package com.inator.calculator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inator.calculator.model.Rate
import com.inator.calculator.repository.PreferenceRepository

class CurrencyInputViewModel(application: Application) : AndroidViewModel(application) {

    private val preferenceRepository = PreferenceRepository.getInstance(application)

    private val outputDirect: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }

    private val outputReverse: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }

    private val currentInput1: MutableLiveData<String> by lazy {

        MutableLiveData("")
    }
    private val currentInput2: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }
    private val spinnerFrom: MutableLiveData<Rate> by lazy {
        MutableLiveData()
    }
    private val spinnerTo: MutableLiveData<Rate> by lazy {
        MutableLiveData()
    }

    fun setSpinner1(rate: Rate) {
        preferenceRepository.saveCurrencySpinner1Prefs(rate)
        spinnerFrom.value = rate
        calculateDirect()
    }

    fun setSpinner2(rate: Rate) {
        preferenceRepository.saveCurrencySpinner2Prefs(rate)
        spinnerTo.value = rate
        calculateDirect()
    }


    fun setInput1(string: String) {
        currentInput1.value = string
        if (!currentInput1.value.isNullOrEmpty()) {
            calculateDirect()
        }
    }

    fun setInput2(string: String) {
        currentInput2.value = string
        if (!currentInput2.value.isNullOrEmpty()) {
            calculateReverse()
        }
    }

    private fun calculateReverse() {
        var output = evaluateExchangeRate(
            currentInput2,
            spinnerTo,
            spinnerFrom
        )
        if (output.endsWith(".0")) {
            output = output.substring(0, output.length - 2)
        }
        outputReverse.value = output
    }

    private fun calculateDirect() {
        var output = evaluateExchangeRate(
            currentInput1,
            spinnerFrom,
            spinnerTo
        )
        if (output.endsWith(".0")) {
            output = output.substring(0, output.length - 2)
        }
        outputDirect.value = output
    }

    fun getOutputDirect(): LiveData<String> = outputDirect

    fun getOutputReverse(): LiveData<String> = outputReverse

    private fun evaluateExchangeRate(
        input: LiveData<String>,
        fromSpinner: LiveData<Rate>,
        toSpinner: LiveData<Rate>
    ): String {
        val fromRate = fromSpinner.value?.value
        val toRate = toSpinner.value?.value
        return if (fromRate == null || toRate == null || input.value.isNullOrEmpty()) {
            "0"
        } else {
            // Round to two decimal places
            String.format("%.2f", input.value?.toDouble()!! * toRate / fromRate)
        }
    }

    fun getSavedSpinner1(): String? {
        return preferenceRepository.getCurrencySpinner1()
    }

    fun getSavedSpinner2(): String? {
        return preferenceRepository.getCurrencySpinner2()
    }
}