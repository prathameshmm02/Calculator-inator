package com.inator.calculator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inator.calculator.R
import com.inator.calculator.repository.ConverterRepository
import com.inator.calculator.repository.PreferenceRepository
import com.inator.calculator.repository.toSimpleString

//This class is similar to CurrencyInputViewModel
class ConverterInputViewModel(application: Application) : AndroidViewModel(application) {

    private val preferenceRepository = PreferenceRepository.getInstance(application)
    private val converterRepository = ConverterRepository.getInstance(application)

    private val measure: MutableLiveData<String> by lazy {
        MutableLiveData("Length")
    }

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
    private val spinnerFrom: MutableLiveData<Int> by lazy {
        MutableLiveData(0)
    }
    private val spinnerTo: MutableLiveData<Int> by lazy {
        MutableLiveData(0)
    }

    fun setMeasure(string: String) {
        preferenceRepository.saveMeasureToPrefs(string)
        measure.value = string
    }

    fun setSpinner1(position: Int) {
        preferenceRepository.saveConverterSpinner1Prefs(position)
        spinnerFrom.value = position
        calculateDirect()
    }

    fun setSpinner2(position: Int) {
        preferenceRepository.saveConverterSpinner2Prefs(position)
        spinnerTo.value = position
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
        var output = evaluateUnitConversion(
            currentInput2,
            spinnerTo,
            spinnerFrom,
            measure
        )
        if (output.endsWith(".0")) {
            output = output.substring(0, output.length - 2)
        }
        outputReverse.value = output
    }

    private fun calculateDirect() {
        var output = evaluateUnitConversion(
            currentInput1,
            spinnerFrom,
            spinnerTo,
            measure
        )
        if (output.endsWith(".0")) {
            output = output.substring(0, output.length - 2)
        }
        outputDirect.value = output
    }

    fun getOutputDirect(): LiveData<String> {
        var output = outputDirect.value
        if (output!!.endsWith(".0")) output = output.substring(0, output.length - 2)
        outputDirect.value = output
        return outputDirect
    }

    fun getOutputReverse(): LiveData<String> {
        var output = outputReverse.value
        if (output!!.endsWith(".0")) output = output.substring(0, output.length - 2)
        outputReverse.value = output
        return outputReverse
    }

    fun getMeasure(): LiveData<String> {
        return measure
    }

    private fun evaluateUnitConversion(
        input: LiveData<String>,
        fromSpinnerPosition: LiveData<Int>,
        toSpinnerPosition: LiveData<Int>,
        measure: LiveData<String>
    ): String {
        if (!input.value.isNullOrEmpty()) {
            return if (measure.value.equals("Temperature")) {
                converterRepository.evaluateTemperature(
                    input,
                    fromSpinnerPosition,
                    toSpinnerPosition
                )
            } else {
                val conversionRates =
                    converterRepository.getConversionValues(measure.value!!)
                val fromRate = conversionRates[fromSpinnerPosition.value!!]
                val toRate = conversionRates[toSpinnerPosition.value!!]
                (input.value?.toDouble()!! * toRate / fromRate).toSimpleString()
            }
        }
        return "0"


    }

    fun getSavedMeasure(): Int {
        //We return the chip Id as per the measure
        return when (preferenceRepository.getMeasurePrefs()) {
            "Length" -> R.id.length
            "Area" -> R.id.area
            "Mass" -> R.id.mass
            "Speed" -> R.id.speed
            "Data" -> R.id.data
            "Volume" -> R.id.volume
            "Time" -> R.id.time
            "Temperature" -> R.id.temperature
            "Angle" -> R.id.angle
            else -> R.id.length
        }
    }

    fun getSavedSpinner1(): Int {
        return preferenceRepository.getConverterSpinner1()
    }

    fun getSavedSpinner2(): Int {
        return preferenceRepository.getConverterSpinner2()
    }

}