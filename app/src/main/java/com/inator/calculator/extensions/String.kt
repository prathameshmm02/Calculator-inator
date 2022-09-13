package com.inator.calculator.extensions

import java.util.regex.Pattern


fun String.isValidNumber(): Boolean {
    //pattern demands format
    // ^(\d*) - starts with either number or no number eg. "12.8" or ".98" are both valid
    // (\.\d+)* - ends with either a dot and a number or nothing
    val p: Pattern = Pattern.compile("^(\\d*)+(\\.\\d+)*\$")
    return p.matcher(this).find()
}

fun String.handleStartingDecimal() =
    if (this.isNotEmpty() && this.substring(0, 1) == ".") "0$this" else this