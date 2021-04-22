package com.inator.calculator.viewmodel

import android.app.Application
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inator.calculator.History.History
import com.inator.calculator.R
import com.inator.calculator.repository.EvaluateString
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class CalculatorInputViewModel(application: Application) : AndroidViewModel(application) {
    private val operators = listOf('^', '%', '÷', '×', '+', '-')
    private val numRegex = "-?[0-9]+\\.?[0-9]*".toRegex()
    private val historyViewModel = HistoryViewModel(application)

    private val inputMutableLiveData = MutableLiveData("")
    val inputLiveData: LiveData<String> = inputMutableLiveData

    private val cursorMutableLiveData = MutableLiveData(0)
    val cursorLiveData: LiveData<Int> = cursorMutableLiveData

    private val outputMutableLiveData = MutableLiveData("")
    val outputLiveData: LiveData<String> = outputMutableLiveData

    private val isDegreeMutableLiveData = MutableLiveData(true)
    val isDegreeLiveData: LiveData<Boolean> = isDegreeMutableLiveData

    private val isInverseMutableLiveData = MutableLiveData(false)
    val isInverseLiveData: LiveData<Boolean> = isInverseMutableLiveData

    fun setCursor(position: Int) {
        cursorMutableLiveData.value = position
    }

    fun numClicked(num: Char) {
        insert(num.toString())
    }

    fun operClicked(oper: Char) {
        insert(oper.toString())
    }

    fun decimalClicked() {
        insert(".")
    }

    fun funClicked(funName: CharSequence) {
        insert("$funName(")
    }

    fun otherClicked(name: CharSequence) {
        insert(name.toString())
    }

    fun otherClicked(name: Char) {
        insert(name.toString())
    }

    fun clearAll() {
        inputMutableLiveData.value = ""
        outputMutableLiveData.value = ""
        cursorMutableLiveData.value = 0
    }

    fun backspaceClicked(selectionStart: Int, selectionEnd: Int) {
        cursorMutableLiveData.value = selectionEnd
        val currentInput = inputMutableLiveData.value
        if (!currentInput.isNullOrEmpty()) {
            if (selectionStart == selectionEnd) {
                inputMutableLiveData.value =
                    currentInput.removeRange(selectionStart, selectionEnd + 1)
                cursorMutableLiveData.value =
                    cursorMutableLiveData.value
                Log.i("removed", " fgdg")
            } else {
                inputMutableLiveData.value =
                    currentInput.removeRange(selectionStart, selectionEnd)
                cursorMutableLiveData.value =
                    cursorMutableLiveData.value?.minus(selectionEnd - selectionStart)
            }

        }

    }

    fun angleClicked() {
        isDegreeMutableLiveData.value = !isDegreeMutableLiveData.value!!
    }

    fun inverseClicked() {
        isInverseMutableLiveData.value = !isInverseMutableLiveData.value!!
    }

    fun equalClicked()/*if we do have to save to history*/ {
        val currentInput = inputLiveData.value!!
        lateinit var result: BigDecimal
        try {
            if (operators.contains(currentInput[currentInput.length - 1])) {
                return
            }
            if (currentInput.isNotEmpty()) {
                result = EvaluateString.evaluate(inputLiveData.value!!, isDegreeLiveData.value!!)
                inputMutableLiveData.value = result.stripTrailingZeros().toPlainString()

            }
            outputMutableLiveData.value = ""
            if (!currentInput.matches(numRegex)){
                saveToHistory(currentInput, result.toPlainString())
            }

        } catch (e: Exception) {
            inputMutableLiveData.value = R.string.error_text.toString()
        }

    }

    fun calculateOutput() {
        val currentInput = inputLiveData.value!!
        lateinit var result: BigDecimal
        try {
            if (currentInput.isNotEmpty()) {
                result = EvaluateString.evaluate(inputLiveData.value!!, isDegreeLiveData.value!!)
                outputMutableLiveData.value = result.stripTrailingZeros().toPlainString()
            } else {
                outputMutableLiveData.value = ""
            }
        } catch (e: Exception) {
        }
    }


    private fun saveToHistory(expression: String, answer: String) {
        val calendar = Calendar.getInstance()
        val date = SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(calendar.time)
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(calendar.time)
        val history = History(expression, answer, date, time)
        historyViewModel.insertHistory(history)
    }

    private fun insert(input: String) {
        val expression = inputMutableLiveData.value
        if (expression.isNullOrEmpty() || cursorMutableLiveData.value == 0) {
            if (!(input == "×" || input == "÷" || input == "^2" || input == "!" || input == "%" || input == "^" || input == ")")) {
                inputMutableLiveData.value = input
                cursorMutableLiveData.value = cursorMutableLiveData.value!! + input.length
            }
        } else {
            //We have to validate user input
            val cursorPosition = cursorMutableLiveData.value!!

            if (cursorPosition == 0) {
                add(input)
            } else {

                val lastChar = expression[cursorPosition - 1]
                if (input.isDigitsOnly()) {
                    add(input)
                    return
                }
                if (operators.contains(input[0])) {
                    if (operators.contains(lastChar)) {
                        if ((lastChar != '+' && input == "-") && !(lastChar == '-' && input == "+")) {
                            add(input)
                        } else {
                            replace(input)
                        }
                    } else {
                        add(input)
                    }

                }
                else {
                    add(input)
                }
            }

        }
    }

    private fun add(input: String) {
        Log.i("Added", "A")

        val expression = inputMutableLiveData.value!!
        val cursorPosition = cursorMutableLiveData.value!!
        if (cursorPosition == expression.length) {
            inputMutableLiveData.value =
                expression.substring(0, cursorPosition) + input
        } else {
            inputMutableLiveData.value =
                expression.substring(0, cursorPosition) + input + expression.substring(
                    cursorPosition
                )
        }

        cursorMutableLiveData.value = cursorMutableLiveData.value?.plus(input.length)
    }

    private fun replace(input: String) {
        Log.i("Replaced", "R")
        val expression = inputMutableLiveData.value!!
        val cursorPosition = cursorMutableLiveData.value!!
        if (cursorPosition == expression.length) {
            inputMutableLiveData.value =
                expression.substring(0, cursorPosition - 1) + input
        } else {
            inputMutableLiveData.value =
                expression.substring(0, cursorPosition - 1) + input + expression.substring(
                    cursorPosition
                )
        }
        cursorMutableLiveData.value = cursorMutableLiveData.value
    }
}