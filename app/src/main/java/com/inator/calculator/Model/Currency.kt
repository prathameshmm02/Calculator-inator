package com.inator.calculator.Model

import android.content.Context

class Currency {
    var base: String? = null
    var date: String? = null
    var rates: Rates? = null
    var time_last_updated = 0
    private var context: Context? = null

    constructor(base: String?, date: String?, rates: Rates?, time_last_updated: Int) {
        this.base = base
        this.date = date
        this.rates = rates
        this.time_last_updated = time_last_updated
    }

    constructor(context: Context?) {
        this.context = context
    }
}