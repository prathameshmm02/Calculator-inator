package com.inator.calculator.repository

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.util.*
import kotlin.math.*

object EvaluateString {
    //Implementing multiplications
    //private val expression = Regex("[[0-9]+\\.?[0-9]+[\\^%÷×+\\-]]*[0-9]+\\.?[0-9]")
    private val numRegex = "-?[0-9]+\\.?[0-9]*".toRegex()
    private val binaryOperators = listOf('^', '%', '÷', '×', '+', '-')
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
            // Current token is a
            // whitespace, skip it
            if (tokens[i] == ' ') {
                i++
                continue
            }

            // Current token is a number,
            // push it to stack for numbers
            when {
                tokens[i] in '0'..'9' -> {
                    val match = numRegex.find(expression.substring(i, expression.length))!!
                    values.push(match.value.toBigDecimal())

                    i += match.range.last - match.range.first

                }
                tokens[i] == '(' -> {
                    if (tokens[i - 1].isDigit() || tokens[i - 1] == 'π' || tokens[i - 1] == 'e') {
                        ops.push(Pair('×', i))
                    }
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
                    values.push(factorial(values.pop()).toBigDecimal())
                }
                tokens[i] == 'π' -> {
                    if (tokens[i - 1].isDigit() || tokens[i - 1] == 'π' || tokens[i - 1] == 'e') {
                        ops.push(Pair('×', i))
                    }
                    values.push(BigDecimal.valueOf(Math.PI))
                }
                tokens[i] == 'e' -> {
                    if (tokens[i - 1].isDigit() || tokens[i - 1] == 'π' || tokens[i - 1] == 'e') {
                        ops.push(Pair('×', i))
                    }
                    values.push(BigDecimal.valueOf(Math.E))
                }
                expression.startsWith("sin⁻¹", i) -> {
                    if (tokens[i - 1].isDigit() || tokens[i - 1] == 'π' || tokens[i - 1] == 'e') {
                        ops.push(Pair('×', i))
                    }
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    //Get Actual Angle if there is an expression inside of a function bracket
                    val value = evaluate(expression.substring(i + 6, closeIndex), isDegree)
                    //sin-1 returns angle so we convert it to current angle unit i.e. Degree or Radians
                    values.push(convertAngle(asin(value.toDouble()), isDegree))
                    i = closeIndex + 1

                }
                expression.startsWith("sin", i) -> {
                    if (tokens[i - 1].isDigit() || tokens[i - 1] == 'π' || tokens[i - 1] == 'e') {
                        ops.push(Pair('×', i))
                    }
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 4, closeIndex), isDegree)
                    values.push(BigDecimal.valueOf(sin(convertAngle(value, isDegree))))
                    i = closeIndex + 1

                }
                expression.startsWith("cos⁻¹", i) -> {
                    if (tokens[i - 1].isDigit() || tokens[i - 1] == 'π' || tokens[i - 1] == 'e') {
                        ops.push(Pair('×', i))
                    }
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 6, closeIndex), isDegree)
                    values.push(convertAngle(acos(value.toDouble()), isDegree))
                    i = closeIndex + 1

                }
                expression.startsWith("cos", i) -> {
                    if (tokens[i - 1].isDigit() || tokens[i - 1] == 'π' || tokens[i - 1] == 'e') {
                        ops.push(Pair('×', i))
                    }
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 4, closeIndex), isDegree)
                    values.push(BigDecimal.valueOf(cos(convertAngle(value, isDegree))))

                    i = closeIndex + 1

                }
                expression.startsWith("tan⁻¹", i) -> {
                    if (tokens[i - 1].isDigit() || tokens[i - 1] == 'π' || tokens[i - 1] == 'e') {
                        ops.push(Pair('×', i))
                    }
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 6, closeIndex), isDegree)
                    values.push(convertAngle(atan(value.toDouble()), isDegree))
                    i = closeIndex + 1

                }
                expression.startsWith("tan", i) -> {
                    if (tokens[i - 1].isDigit() || tokens[i - 1] == 'π' || tokens[i - 1] == 'e') {
                        ops.push(Pair('×', i))
                    }
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 4, closeIndex), isDegree)
                    values.push(BigDecimal.valueOf(tan(convertAngle(value, isDegree))))

                    i = closeIndex + 1

                }
                expression.startsWith("log", i) -> {
                    if (tokens[i - 1].isDigit() || tokens[i - 1] == 'π' || tokens[i - 1] == 'e') {
                        ops.push(Pair('×', i))
                    }
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 4, closeIndex), isDegree)
                    values.push(BigDecimal.valueOf(log10(value.toDouble())))
                    i = closeIndex + 1

                }
                expression.startsWith("ln", i) -> {
                    if (tokens[i - 1].isDigit() || tokens[i - 1] == 'π' || tokens[i - 1] == 'e') {
                        ops.push(Pair('×', i))
                    }
                    val closeIndex =
                        i + getIndexOfCloseBrack(expression.substring(i, expression.length))
                    val value = evaluate(expression.substring(i + 3, closeIndex), isDegree)
                    values.push(BigDecimal.valueOf(ln(value.toDouble())))
                    i = closeIndex + 1

                }
                tokens[i] == '√' -> {
                    if (tokens[i - 1].isDigit() || tokens[i - 1] == 'π' || tokens[i - 1] == 'e') {
                        ops.push(Pair('×', i))
                    }
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
                        tokens[i].isDigit() -> {
                            val match = numRegex.find(expression.substring(i, expression.length))!!
                            values.push(sqrt(match.value.toDouble()).toBigDecimal())
                            i += match.range.last - match.range.first
                        }
                        tokens[i] == 'e' -> {
                            values.push(sqrt(E).toBigDecimal())
                        }
                        tokens[i] == 'π' -> {
                            values.push(sqrt(PI).toBigDecimal())
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
            '%' -> 3
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
            '+' -> return a.add(b)
            '-' -> return a.subtract(b)
            '×' -> return a.multiply(b)
            '÷' -> {
                if (b == BigDecimal.ZERO) throw UnsupportedOperationException(
                    "Cannot divide by zero"
                )
                return a.divide(b, MathContext.DECIMAL64)
            }
            '%' -> {
                return a.multiply(b).divide(BigDecimal(100), MathContext.DECIMAL64)
            }
            '^' -> {
                return a.toDouble().pow(b.toDouble()).toBigDecimal()
            }
        }
        return BigDecimal.ZERO
    }

    private fun factorial(number: BigDecimal): BigInteger {
        var fact = BigInteger.ONE
        for (factor in 2..number.toLong()) {
            fact = fact.multiply(BigInteger.valueOf(factor))
        }
        return fact
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
            Math.toRadians(angle).toBigDecimal()
        } else {
            angle.toBigDecimal()
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