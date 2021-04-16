package com.inator.calculator.Model

import com.google.gson.annotations.SerializedName
import java.util.*


data class Currency(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("base")
    val base: String?,
    @SerializedName("date")
    val date: Date?,
    @SerializedName("rates")
    val rates: List<Rate>?
)