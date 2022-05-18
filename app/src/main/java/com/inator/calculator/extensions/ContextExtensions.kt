package com.inator.calculator.extensions

import android.content.Context
import android.content.res.Configuration

val Context.isLandscape get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

val Context.smallestScreenWidthDp get() = resources.configuration.smallestScreenWidthDp