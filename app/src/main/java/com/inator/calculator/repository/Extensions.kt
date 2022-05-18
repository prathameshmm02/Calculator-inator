package com.inator.calculator.repository

import java.math.BigDecimal

fun String.toExpression(): String {
    return replace('×', '*')
        .replace('÷', '/')
        .replace("π", "pi")
        .replace("sin⁻¹", "asin")
        .replace("cos⁻¹", "acos")
        .replace("tan⁻¹", "atan")
        .replace("log", "log10")
        .maybeAppendClosedBrackets()
}

fun Double.toSimpleString(): String {
    return toBigDecimal().toSimpleString()
}

fun BigDecimal.toSimpleString(): String {
    return toPlainString().removeSuffix(".0")
}

private fun String.maybeAppendClosedBrackets(): String {
    var expression = this

    var open = 0
    var close = 0

    for (i in 0 until length) {
        if (this[i] == '(') {
            open++
        } else if (this[i] == ')') {
            close++
        }
    }
    if (open - close > 0) {
        for (j in 0 until open - close) expression += ")"
    } else if (close - open > 0) {
        for (j in 0 until close - open) expression = "($expression"
    }

    return expression
}

