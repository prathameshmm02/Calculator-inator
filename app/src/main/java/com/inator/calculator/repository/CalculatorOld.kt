package com.inator.calculator.repository

import android.util.Log
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import java.util.*
import kotlin.math.*
//  Older Implementation of Math String Evaluator. Not that good has some mistakes
class CalculatorOld {
    private val operators = listOf('^', '%', '÷', '×', '+', '-')
    private val numbers = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.')
    private val pi: BigDecimal = BigDecimal.valueOf(Math.PI)
    private val e = BigDecimal.valueOf(Math.E)
    private val rounder = BigDecimal("1000000000000000")
    private val numRegex = "[0-9]+/.?[0-9]+".toPattern()
    fun solve(expr: String?, isDegree: Boolean): BigDecimal {
        val operatorList: MutableList<Char> = ArrayList()
        val operandList: MutableList<BigDecimal> = ArrayList()
        var num = ""
        // Filtering Operands and Operators
        var isNegative = false
        var i = 0
        while (i < expr!!.length) {

            val current = expr[i]

            if (numbers.contains(current)) {
                num += current
            } else if (current == '√' || current == '!') {
                if (current == '√') {

                }
                operatorList.add(current)
            } else if (current == 'π') {

                operandList.add(pi)
            } else if (current == 'e') {
                operandList.add(e)
            } else if (expr.startsWith("sin⁻¹", i)) {

                val index = expr.indexOf(")", i)
                val value = expr.substring(i + 6, index).toDouble()

                operandList.add(convertAngle(BigDecimal.valueOf(asin(value)), isDegree))
                i = index
            } else if (expr.startsWith("cos⁻¹", i)) {
                val index = expr.indexOf(")", i)
                val value = convertAngle(expr.substring(i + 6, index), isDegree)
                operandList.add(convertAngle(BigDecimal.valueOf(acos(value)), isDegree))
                i = index
            } else if (expr.startsWith("tan⁻¹", i)) {
                val index = expr.indexOf(")", i)
                val value = convertAngle(expr.substring(i + 6, index), isDegree)
                operandList.add(convertAngle(BigDecimal.valueOf(atan(value)), isDegree))
                i = index
            } else if (expr.startsWith("sin", i)) {
                val index = expr.indexOf(")", i)
                val angle = convertAngle(expr.substring(i + 4, index), isDegree)
                operandList.add(BigDecimal.valueOf(sin(angle)))
                i = index
            } else if (expr.startsWith("cos", i)) {
                val index = expr.indexOf(")", i)
                val angle = convertAngle(expr.substring(i + 4, index), isDegree)
                operandList.add(BigDecimal.valueOf(cos(angle)))
                i = index
            } else if (expr.startsWith("tan", i)) {
                val index = expr.indexOf(")", i)
                val angle = convertAngle(expr.substring(i + 4, index), isDegree)
                operandList.add(BigDecimal.valueOf(tan(angle)))
                i = index
            } else if (expr.startsWith("log", i)) {
                val index = expr.indexOf(")", i)
                operandList.add(
                    BigDecimal.valueOf(
                        log10(
                            expr.substring(i + 4, index).toDouble()
                        )
                    )
                )
                i = index
            } else if (expr.startsWith("ln", i)) {

                val index = expr.indexOf(")", i)
                operandList.add(
                    BigDecimal.valueOf(
                        ln(
                            expr.substring(i + 3, index).toDouble()
                        )
                    )
                )
                i = index
            } else if (current == '(' || current == ')') {
                i++
                continue
            } else {
                if (num.isNotEmpty()) {
                    if (!isNegative) {
                        operandList.add(BigDecimal.valueOf(num.toDouble()))
                    } else {
                        isNegative = false
                        operandList.add(BigDecimal.valueOf(num.toDouble()).negate())
                    }
                }
                if (current == '-') {
                    isNegative = true
                    //char lastOperation = operatorList.get(operatorList.size()-1);
                    if (i != 0) {
                        val lastChar = expr[i - 1]
                        if (lastChar != '×' && lastChar != '÷') {
                            operatorList.add('+')
                        }
                    }
                } else {
                    if (operators.contains(current)) {
                        operatorList.add(current)
                    }
                }
                num = ""
            }

            i++
        }
        // Adding last operand
        if (num.isNotEmpty()) {
            if (!isNegative) {
                operandList.add(BigDecimal(num))
            } else {
                operandList.add(BigDecimal(num).negate())
            }
        }

        Log.i(operandList.toString(), operatorList.toString())
        // Solving (Actually)
        for (operator in operators) {

            var num1: BigDecimal
            var num2: BigDecimal
            var answer: BigDecimal
            while (operatorList.contains(operator)) {
                val index = operatorList.indexOf(operator)
                num1 = operandList[index]
                num2 = try {
                    operandList[index + 1]
                } catch (e: IndexOutOfBoundsException) {
                    return num1
                }
                answer = when (operator) {

                    '÷' -> {
                        num1.divide(num2, MathContext.DECIMAL128)
                    }
                    '×' -> {
                        num1.multiply(num2)
                    }
                    '+' -> {
                        num1.add(num2)
                    }
                    '-' -> {
                        num1.subtract(num2)
                    }
                    '^' -> {
                        BigDecimal.valueOf(num1.toDouble().pow(num2.toDouble()))
                    }
                    '%' -> {
                        num1.multiply(num2)
                            .divide(BigDecimal.valueOf(100), MathContext.DECIMAL128)
                    }

                    else -> {
                        num1
                    }
                }
                operandList.removeAt(index)
                operatorList.removeAt(index)
                operandList.removeAt(index)
                operandList.add(index, answer)
            }
            Log.i(operandList.toString(), operatorList.toString())
        }
        return operandList[0].multiply(rounder).setScale(0, RoundingMode.HALF_UP).divide(rounder)
    }

    //This will solve all brackets for ya
    private fun solveBrackets(expression: String, isDegree: Boolean): String {
        var expr = expression
        val indices = Stack<Int>()
        var result: BigDecimal
        var index = 0
        for (i in expr.indices) {
            if (expr[i] == '(') {
                indices.push(i)
            } else if (expr[i] == ')') {
                if (!indices.isEmpty()) {
                    index = indices.pop()
                }
                result = expr.substring(index + 1, i).matches(numRegex.toRegex()).let {
                    if (it) {
                        BigDecimal(expr.substring(index + 1, i))
                    } else {
                        eval(expr.substring(index + 1, i), isDegree)
                    }
                }
                expr = if (indices.isEmpty()) {
                    expr.substring(0, index + 1) + result.toString() + expr.substring(i)
                } else {
                    expr.substring(0, index) + result.toString() + expr.substring(i + 1)
                }
                Log.i("Expr", expr)
            }
            if (i == expr.length - 1 && !indices.isEmpty()) {
                index = indices.pop()
                result = eval(expr.substring(index + 1), isDegree)
                expr = expr.substring(0, index) + result.toString()
            }
        }
        return expr
    }

    //this is actually like a Javascript eval function
    fun eval(expr: String, isDegree: Boolean): BigDecimal {
        return solve(solveBrackets(expr, isDegree), isDegree)
    }

    private fun factorial(number: BigInteger): BigInteger {
        var fact = BigInteger.ONE
        for (factor in 2..number.toLong()) {
            fact = fact.multiply(BigInteger.valueOf(factor))
        }
        return fact
    }

    private fun convertAngle(angleString: String, isDegree: Boolean): Double {
        val angle = angleString.toDouble()
        return if (isDegree) {
            Math.toRadians(angle)
        } else {
            angle
        }
    }

    private fun convertAngle(angleBig: BigDecimal, isDegree: Boolean): BigDecimal {
        val angle = angleBig.toDouble()
        return if (isDegree) {
            BigDecimal.valueOf(Math.toRadians(angle))
        } else {
            BigDecimal.valueOf(angle)
        }
    }

    companion object {
        fun validateInput(
            currentPosition: Int,
            buttonText: String,
            oldInput: String
        ): BooleanArray {
            val operators2 = listOf("^", "%", "÷", "×", "+", "-")
            val extraOperators =
                listOf("sin", "cos", "tan", "log", "ln", "sin⁻¹", "cos⁻¹", "tan⁻¹")
            val powOperators = listOf("^2", "e^", "10^")
            val numbers2 = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
            val validations = BooleanArray(2)
            var lastChar = 0.toChar()
            return if (oldInput.isNotEmpty()) {
                if (currentPosition != 0) {
                    lastChar = oldInput[currentPosition - 1]
                }
                if (extraOperators.contains(buttonText)) {
                    validations[0] = true
                    validations[1] = true
                    validations
                } else if (powOperators.contains(buttonText)) {
                    validations[0] = true
                    validations
                } else if (!operators2.contains(buttonText) || !operators2.contains(lastChar.toString()) && lastChar != '.') {
                    validations[0] = true
                    validations
                } else if (buttonText != "-" || lastChar.toInt() != 215 && lastChar.toInt() != 247) {
                    validations
                } else {
                    validations[0] = true
                    validations
                }
            } else if (extraOperators.contains(buttonText)) {
                validations[0] = true
                validations[1] = true
                validations
            } else if (powOperators.contains(buttonText)) {
                validations[0] = true
                validations
            } else if (buttonText != "-" && buttonText != "e" && buttonText != "π" && buttonText != "(" && buttonText != "√" && !numbers2.contains(
                    buttonText
                )
            ) {
                validations
            } else {
                validations[0] = true
                validations
            }
        }
    }
}