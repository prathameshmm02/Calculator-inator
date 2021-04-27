package com.inator.calculator.repository


import android.util.Log
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.math.*


object EvaluateString {
    //private val expression = Regex("[[0-9]+\\.?[0-9]+[\\^%÷×+\\-]]*[0-9]+\\.?[0-9]")
    private val numRegex = "-?[0-9]+\\.?[0-9]*".toRegex()
    private val binaryOperators = listOf('^', '÷', '×', '+', '-')
    fun evaluate(expression: String, isDegree: Boolean): BigDecimal {
        val tokens = expression.toCharArray()

        // Stack for numbers: 'values'
        val values = Stack<BigDecimal>()

        // Stack for Operators: 'ops'
        val ops = Stack<Pair<Char, Int>>()

        //Stack for brackets
        val brackets = Stack<Int>()

        var i = 0
        while (i < tokens.size) {
            Log.i("i", i.toString())
            Log.i("ovaps", ops.toString())
            Log.i("values", values.toString())

            // Current token is a number, check for next numbers
            // push it to stack for numbers
            when {
                tokens[i] in '0'..'9' -> {
                    val match = numRegex.find(expression.substring(i, expression.length))!!
                    var prev: Char? = null
                    var prev2: Char? = null
                    if (i > 0) {
                        prev = tokens[i - 1]
                    }
                    if (i > 1) {
                        prev2 = tokens[i - 2]
                    }
                    if (prev == '-' && prev2 == null) {
                        values.push(-match.value.toBigDecimal())
                        ops.pop()
                    } else if (prev == '-' && binaryOperators.contains(prev2))
                        values.push(-match.value.toBigDecimal())
                    else
                        values.push(match.value.toBigDecimal())

                    i += match.range.last - match.range.first
                    i++
                    if (i == tokens.size) {
                        break
                    }
                    //Just handling 4e or 5sin(90) cases
                    if (!(binaryOperators.contains(tokens[i]) || tokens[i] == '!' || tokens[i] == '%')) {
                        ops.push(Pair('×', i - 1))
                    }
                    i--
                }
                tokens[i] == '(' -> {
                    brackets.push(i)
                }
                tokens[i] == ')' -> {
                    while (brackets.peek() < ops.peek().second) values.push(
                        applyOp(
                            ops.pop().first,
                            values.pop(),
                            values.pop()
                        )
                    )
                    brackets.pop()
                }
                binaryOperators.contains(tokens[i]) -> {
                    // While top of 'ops' has same
                    // or greater precedence to current
                    // token, which is an operator.
                    // Apply operator on top of 'ops'
                    // to top two elements in values stack
                    if (i > 0) {
                        val prev = tokens[i - 1]
                        if (tokens[i] == '-' && binaryOperators.contains(prev)) {
                            i++
                            continue
                        }
                    }
                    while (!ops.empty() &&
                        hasPrecedence(
                            tokens[i],
                            ops.peek().first
                        )
                    ) values.push(
                        applyOp(
                            ops.pop().first,
                            values.pop(),
                            values.pop()
                        )
                    )

                    // Push current token to 'ops'.
                    ops.push(Pair(tokens[i], i))
                }
                tokens[i] == '!' -> {
                    values.push(factorial(values.pop().toBigInteger()))

                }
                tokens[i] == '%' -> {
                    values.push(values.pop().divide(BigDecimal.valueOf(100)))
                }
                tokens[i] == 'π' -> {
                    values.push(BigDecimal.valueOf(Math.PI))
                    i++
                    if (i == tokens.size) {
                        break
                    }
                    if (!(binaryOperators.contains(tokens[i]) || tokens[i] == '!' || tokens[i] == '%' || tokens[i].isDigit())) {
                        ops.push(Pair('×', i - 1))
                    }
                    i--
                }
                tokens[i] == 'e' -> {
                    values.push(BigDecimal.valueOf(Math.E))
                    i++
                    if (i == tokens.size) {
                        break
                    }
                    //Just handling 4e or 5sin(90) cases
                    if (!(binaryOperators.contains(tokens[i]) || tokens[i] == '!' || tokens[i] == '%' || tokens[i].isDigit())) {
                        ops.push(Pair('×', i - 1))
                    }
                    i--

                }
                expression.startsWith("sin⁻¹", i) -> {
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    //Get Actual Angle if there is an expression inside of a function bracket
                    val value = evaluate(expression.substring(i + 6, closeIndex), isDegree)
                    //sin-1 returns angle so we convert it to current angle unit i.e. Degree or Radians
                    values.push(convertAngle(asin(value.toDouble()), isDegree))
                    i = closeIndex + 1

                }
                expression.startsWith("sin", i) -> {
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 4, closeIndex), isDegree)
                    values.push(BigDecimal.valueOf(sin(convertAngle(value, isDegree))))
                    i = closeIndex + 1

                }
                expression.startsWith("cos⁻¹", i) -> {
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 6, closeIndex), isDegree)
                    values.push(convertAngle(acos(value.toDouble()), isDegree))
                    i = closeIndex + 1

                }
                expression.startsWith("cos", i) -> {
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 4, closeIndex), isDegree)
                    values.push(BigDecimal.valueOf(cos(convertAngle(value, isDegree))))

                    i = closeIndex + 1

                }
                expression.startsWith("tan⁻¹", i) -> {
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 6, closeIndex), isDegree)
                    values.push(convertAngle(atan(value.toDouble()), isDegree))

                    i = closeIndex + 1

                }
                expression.startsWith("tan", i) -> {
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 4, closeIndex), isDegree)
                    values.push(BigDecimal.valueOf(tan(convertAngle(value, isDegree))))


                    i = closeIndex + 1

                }
                expression.startsWith("log", i) -> {
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 4, closeIndex), isDegree)
                    values.push(BigDecimal.valueOf(log10(value.toDouble())))
                    i = closeIndex + 1

                }
                expression.startsWith("ln", i) -> {
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 3, closeIndex), isDegree)
                    values.push(BigDecimal.valueOf(ln(value.toDouble())))

                    i = closeIndex + 1

                }
                tokens[i] == '√' -> {
                    i++
                    when {
                        tokens[i] == '(' -> {
                            val closeIndex =
                                i + getIndexOfCloseBrack(
                                    expression.substring(
                                        i,
                                        expression.length
                                    )
                                )
                            val value = evaluate(expression.substring(i, closeIndex), isDegree)
                            values.push(BigDecimal.valueOf(sqrt(value.toDouble())))
                            i = closeIndex + 1
                        }
                        tokens[i].isLetter() && tokens[i] != 'e' && tokens[i] != 'π' -> {
                            val closeIndex = i + getIndexOfCloseBrack(
                                expression.substring(
                                    expression.indexOf(
                                        '(',
                                        i
                                    )
                                )
                            )
                            val value = evaluate(expression.substring(i, closeIndex), isDegree)
                            values.push(BigDecimal.valueOf(sqrt(value.toDouble())))
                            i = closeIndex + 1
                        }
                        else -> {
                            var closeIndex = 0
                            for (index in i until tokens.size) {
                                if (binaryOperators.contains(tokens[index])) {
                                    closeIndex = i + index
                                    break
                                }
                            }
                            if (closeIndex == 0) closeIndex = tokens.size
                            val value = evaluate(expression.substring(i, closeIndex), isDegree)
                            values.push(BigDecimal.valueOf(sqrt(value.toDouble())))
                            i = closeIndex + 1
                        }
                    }
                }
            }
            i++
        }

        // Entire expression has been
        // parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty()) values.push(
            applyOp(
                ops.pop().first,
                values.pop(),
                values.pop()
            )
        )

        // Top of 'values' contains
        // result, return it
        return values.pop()
    }

    // Returns true if 'op2' has higher
    // or same precedence as 'op1',
    // otherwise returns false.
    private fun hasPrecedence(
        op1: Char, op2: Char
    ): Boolean {
        return getPrecedence(op1) < getPrecedence(op2)
    }

    private fun getPrecedence(op: Char): Int {
        return when (op) {
            '^' -> 4
            '÷' -> 3
            '×' -> 2
            '+' -> 1
            '-' -> 1
            else -> 0
        }
    }

    // A utility method to apply an
// operator 'op' on operands 'a'
// and 'b'. Return the result.
    private fun applyOp(
        op: Char,
        b: BigDecimal, a: BigDecimal
    ): BigDecimal {
        when (op) {
            '+' -> return a + b
            '-' -> return a - b
            '×' -> return a * b
            '÷' -> {
                if (b == BigDecimal.ZERO) throw UnsupportedOperationException(
                    "Cannot divide by zero"
                )
                return a / b
            }
            '^' -> {
                return a.toDouble().pow(b.toDouble()).toBigDecimal()
            }
        }
        return BigDecimal.ZERO
    }

    private fun factorial(number: BigInteger): BigDecimal {
        var fact = BigInteger.ONE
        for (factor in 2..number.toLong()) {
            fact = fact.multiply(factor.toBigInteger())
        }
        return fact.toBigDecimal()
    }


    private fun getIndexOfCloseBrack(arg: String): Int {
        var index = 0
        val brackets = Stack<Int>()
        while (index < arg.length) {
            if (arg[index] == '(') {
                brackets.push(index)
            } else if (arg[index] == ')') {
                if (brackets.size == 1) {
                    return brackets.pop()
                } else {
                    brackets.pop()
                }
            }
            index++
        }
        return index
    }


    private fun convertAngle(angle: Double, isDegree: Boolean): BigDecimal {
        return if (isDegree) {
            BigDecimal.valueOf(Math.toRadians(angle))
        } else {
            BigDecimal.valueOf(angle)
        }
    }

    private fun convertAngle(angle: BigDecimal, isDegree: Boolean): Double {
        return if (isDegree) {
            Math.toRadians(angle.toDouble())
        } else {
            angle.toDouble()
        }
    }
}