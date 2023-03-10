package com.inator.calculator.extensions

import java.util.regex.Pattern


fun String.isValidNumber(): Boolean {
    val p: Pattern = Pattern.compile("^-?(\\d*)+(\\.\\d+)*\$")
    return p.matcher(this).find()
}

fun String.handleStartingDecimal() =
    if (this.isNotEmpty() && this.substring(0, 1) == ".") "0$this"
    else this