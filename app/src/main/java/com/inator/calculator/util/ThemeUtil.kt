package com.inator.calculator.util

import androidx.appcompat.app.AppCompatDelegate

fun setTheme(theme: String) {
    AppCompatDelegate.setDefaultNightMode(
        when (theme) {
            "0" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            "1" -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_YES
        }
    )
}
