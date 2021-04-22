package com.inator.calculator.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.inator.calculator.model.Currency
import com.inator.calculator.model.Rate
import java.lang.reflect.Type
import java.util.*

class CurrencyDeserializer : JsonDeserializer<Currency> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Currency {
        if (json == null || context ==null){
            throw Exception("Error")
        }
        val obj = json.asJsonObject

        // let Gson handle the other 3 properties
        val success = context.deserialize<Boolean?>(obj.get("success"), Boolean::class.java)
        val base = context.deserialize<String?>(obj.get("base"), String::class.java)
        val date = context.deserialize<Date?>(obj.get("date"), Date::class.java)

        // create List<Rate> from the rates JsonObject
        val ratesSet = obj.get("rates").asJsonObject.entrySet()
        val ratesList = ratesSet.map {
            val code = it.key
            val value = it.value.asFloat
            Rate(code, value)
        }
        return Currency(success, base, date, ratesList)

    }

}
