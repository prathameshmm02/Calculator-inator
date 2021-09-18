package com.inator.calculator.repository

fun String.toExpression(): String {
    return replace('×', '*')
        .replace('÷', '/')
        .replace("π", "pi")
}

fun Double.toSimpleString(): String {
    var result = this.toString()
    result.indexOf(".0").let {
        if (it != -1) {
            result = result.substring(0, it)
        }
    }
    return result
}

