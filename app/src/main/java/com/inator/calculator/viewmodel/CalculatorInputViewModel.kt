package com.inator.calculator.viewmodel

import android.app.Application
import android.content.Context
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inator.calculator.R
import com.inator.calculator.model.History
import com.inator.calculator.repository.toExpression
import com.inator.calculator.repository.toSimpleString
import org.mariuszgromada.math.mxparser.Expression
import java.text.SimpleDateFormat
import java.util.*

class CalculatorInputViewModel(application: Application) : AndroidViewModel(application) {
    private val operators = listOf('^', '÷', '×', '+', '-')
    private val numRegex = "-?[0-9]+\\.?[0-9]*".toRegex()
    private val historyViewModel = HistoryViewModel(application)

    private val inputMutableLiveData = MutableLiveData("")
    val inputLiveData: LiveData<String> = inputMutableLiveData

    private val cursorMutableLiveData = MutableLiveData(0)
    val cursorLiveData: LiveData<Int> = cursorMutableLiveData

    private val outputMutableLiveData = MutableLiveData("")
    val outputLiveData: LiveData<String> = outputMutableLiveData

    private val isInverseMutableLiveData = MutableLiveData(false)
    val isInverseLiveData: LiveData<Boolean> = isInverseMutableLiveData

    private var isDecimal = false

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
        if (!isDecimal) {
            isDecimal = true
            insert(".")
        }

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
        isDecimal = false
        inputMutableLiveData.value = ""
        outputMutableLiveData.value = ""
        cursorMutableLiveData.value = 0
    }

    fun backspaceClicked(expression: String, selectionStart: Int) {
        val oldExpression = inputMutableLiveData.value
        if (oldExpression!!.replace(expression, "") == ".") {
            isDecimal = false
        }
        inputMutableLiveData.value = expression
        cursorMutableLiveData.value = selectionStart
    }

    fun inverseClicked() {
        isInverseMutableLiveData.value = !isInverseLiveData.value!!
    }

    fun equalClicked(context: Context)  /*if we do have to save to history*/ {
        val currentInput = inputLiveData.value!!
        var result = 0.0
        try {
            if (operators.contains(currentInput[currentInput.length - 1])) {
                return
            }
            if (currentInput.isNotEmpty()) {
                result =
                    Expression(inputLiveData.value!!.toExpression()).calculate()
                inputMutableLiveData.value = result.toSimpleString()
            }
            outputMutableLiveData.value = ""
            cursorMutableLiveData.value = inputLiveData.value?.length ?: 0
            if (!currentInput.matches(numRegex)) {
                saveToHistory(currentInput, result.toSimpleString())
            }
        } catch (e: Exception) {
            inputMutableLiveData.value = context.getString(R.string.error_text)
        }
    }

    fun calculateOutput() {
        val currentInput = inputLiveData.value!!
        val result: Double
        try {
            if (currentInput.matches(numRegex)) {
                outputMutableLiveData.value = ""
            } else if (currentInput.isNotEmpty()) {
                result =
                    Expression(inputLiveData.value!!.toExpression()).calculate()
                if (result.toSimpleString() != "NaN") {
                    outputMutableLiveData.value = result.toSimpleString()
                }
            } else {
                outputMutableLiveData.value = ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun saveToHistory(expression: String, answer: String) {
        val calendar = Calendar.getInstance()
        val date = SimpleDateFormat("dd MMM", Locale.getDefault()).format(calendar.time)
        val history = History(expression, answer, date)
        historyViewModel.insertHistory(history)
    }

    private fun insert(input: String) {
        val expression = inputLiveData.value ?: ""
        if (!input.isDigitsOnly() && input != ".") {
            isDecimal = false
        }
        if (cursorLiveData.value == 0) {
            if (!(input == "×" || input == "÷" || input == "^2" || input == "!" || input == "%" || input == "^" || input == ")")) {
                if (expression.isEmpty()) {
                    inputMutableLiveData.value = input
                    cursorMutableLiveData.value = cursorLiveData.value!! + input.length
                } else {
                    add(input)
                }
            }
        } else {
            //We have to validate user input
            val cursorPosition = cursorLiveData.value!!

            val lastChar = expression[cursorPosition - 1]
            if (input.isDigitsOnly()) {
                add(input)
            } else if (operators.contains(input[0])) {
                if (operators.contains(lastChar)) {
                    if (cursorPosition > 2) {
                        if (operators.contains(expression[cursorPosition - 2]) && lastChar == '-') {
                            return
                        }
                    }
                    if (lastChar == '-' && input == "-") {
                        return
                    }
                    if ((lastChar != '+' && input == "-") && !(lastChar == '-' && input == "+")) {
                        add(input)
                    } else {
                        replace(input)
                    }
                } else {
                    add(input)
                }
            } else {
                add(input)
            }
        }
    }

    private fun add(input: String) {
        val expression = inputLiveData.value!!
        val cursorPosition = cursorLiveData.value!!
        if (cursorPosition == expression.length) {
            inputMutableLiveData.value =
                expression.substring(0, cursorPosition) + input
        } else {
            inputMutableLiveData.value =
                expression.substring(0, cursorPosition) + input + expression.substring(
                    cursorPosition
                )
        }
        cursorMutableLiveData.value = cursorLiveData.value?.plus(input.length)
    }

    private fun replace(input: String) {
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