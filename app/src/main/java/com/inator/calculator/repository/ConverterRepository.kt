package com.inator.calculator.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.inator.calculator.R
import org.mariuszgromada.math.mxparser.Expression

class ConverterRepository(context: Context) {

    private var mResources = context.resources

    companion object {

        private var instance: ConverterRepository? = null

        fun getInstance(context: Context): ConverterRepository {
            if (instance == null) {
                synchronized(ConverterRepository::class) {
                    instance = ConverterRepository(context)
                }
            }
            return instance!!
        }
    }

    fun getConversionValues(measure: String): List<Double> {

        val resValues = when (measure) {
            "Length" -> R.array.length_rates
            "Area" -> R.array.area_rates
            "Mass" -> R.array.mass_rates
            "Speed" -> R.array.speed_rates
            "Pressure" -> R.array.pressure_rates
            "Data" -> R.array.storage_rates
            "Volume" -> R.array.volume_rates
            "Time" -> R.array.time_rates
            "Angle" -> R.array.angle_rates
            else -> R.array.length_rates
        }
        val values = mResources.getStringArray(resValues)

        //Converting String Array to Double Array
        return values.map { it.evaluate() }
    }

    fun evaluateTemperature(
        input: LiveData<String>,
        fromSpinnerPosition: LiveData<Int>,
        toSpinnerPosition: LiveData<Int>,
    ): String {
        val doubleInput: Double = input.value!!.toDouble()
        val toMain: Double =
            when (mResources.getStringArray(R.array.temperature_units)[fromSpinnerPosition.value!!]) {
                //Our Main Unit is Celsius here, we first convert given input to main unit hence toMain and then we'll convert it from main to other unit
                "Celsius" -> {
                    doubleInput
                }
                "Fahrenheit" -> {
                    (doubleInput - 32) * (5 / 9.0)
                }
                "Kelvin" -> {
                    doubleInput - 273.15
                }
                else -> 0.0
            }
        return when (mResources.getStringArray(R.array.temperature_units)[toSpinnerPosition.value!!]) {
            "Celsius" -> {
                toMain
            }
            "Fahrenheit" -> {
                toMain * (9 / 5.0) + 32
            }
            else -> {
                toMain + 273.15
            }
        }.toSimpleString()
    }
}

private fun String.evaluate(): Double {
    return if (!contains('*') && !contains('/')) toDouble()
    else {
        Expression(this).calculate()
    }
}
