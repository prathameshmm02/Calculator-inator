package com.inator.calculator.Data

import com.inator.calculator.Model.Measure
import com.inator.calculator.Model.Rates
import com.inator.calculator.StartUp

object ConvertData {
    private lateinit var length: Measure
    private lateinit var area: Measure
    private lateinit var mass: Measure
    private lateinit var speed: Measure
    private lateinit var data: Measure
    private lateinit var volume: Measure
    private lateinit var time: Measure
    private lateinit var currency: Measure
    private lateinit var temperature: Measure
    private lateinit var angle: Measure



    fun getLength(): Measure {
        length = Measure("Length")
        length.units =
            arrayOf(
                "Meter",
                "Kilometre",
                "Centimeter",
                "Foot",
                "Mile",
                "Inch",
                "Yard",
                "Decimeter",
                "Millimetre",
                "Micrometre",
                "Nanometre",
                " Picometre",
                "Nautical Mile",
                "Lunar Distance",
                "Astronomical Mile",
                "Light Year"
            )
        
        length.fromMain = doubleArrayOf(
            1.0,
            0.001,
            100.0,
            3.28084,
            0.000621371,
            39.3701,
            1.09361,
            10.0,
            1000.0,
            1000000.0,
            1000000000.0,
            1000000000000.0,
            0.000539957,
            0.00000000260201912116327,
            0.00000000000668458712,
            0.00000000000000010570008340246155
        )

        length.toMain = calculateToMain(length)
        return length
    }

    fun getMass(): Measure {
        mass = Measure("Mass")
        mass.units = arrayOf(
                "Kilogram",
                "Gram",
                "Tonne",
                "Pound",
                "Ounce",
                "Milligram",
                "Microgram",
                "US Ton",
                "Quintal",
                "Imperial Ton",
                "Stone"
            )
        
        mass.fromMain =
            doubleArrayOf(
                1.0,
                1000.0,
                0.001,
                2.20462,
                35.274,
                1000000.0,
                1000000000.0,
                0.00110231,
                0.01,
                0.000984207,
                0.157473119999996608
            )
        
        mass.toMain = calculateToMain(mass)
        return mass
    }

    fun getArea(): Measure {
        area = Measure("Area")
        area.units = (
            arrayOf(
                "Square Meter",
                "Square Kilometre",
                "Acre",
                "Hectare",
                "Square Mile",
                "Square Yard",
                "Square Foot",
                "Square Inch"
            )
        )
        area.fromMain =
            doubleArrayOf(
                1.0,
                0.000001,
                0.000247105,
                0.0001,
                0.000000386102,
                1.19599,
                1550.0,
                10.7639
            )
        area.toMain = (calculateToMain(area))
        return area
    }

    fun getAngle(): Measure {
        angle = Measure("Angle")
        angle.units = (
            arrayOf(
                "Degrees",
                "Radians",
                "Gradian",
                "Milliradian",
                "Minute of Arc",
                "Second of Arc"
            )
        )
        angle.fromMain = doubleArrayOf(1.0, Math.PI / 180, 1.11111, 17.4533, 60.0, 3600.0)
        angle.toMain = (calculateToMain(angle))
        return angle
    }

    fun getSpeed(): Measure {
        speed = Measure("Speed")
        speed.units = (
            arrayOf(
                "Meter per second",
                "Kilometer per hour",
                "Miles per hour",
                "Foot per second",
                "Knot"
            )
        )
        speed.fromMain = doubleArrayOf(1.0, 3.6, 0.621371, 0.911344, 0.539957)
        speed.toMain = (calculateToMain(speed))
        return speed
    }

    fun getData(): Measure {
        data = Measure("Data")
        data.units = (
            arrayOf(
                "Bit",
                "Kilobit",
                "Kibibit",
                "Megabit",
                "Mebibit",
                "Gigabit",
                "Gibibit",
                "Terabit",
                "Tebibit",
                "Petabit",
                "Pebibit",
                "Byte",
                "Kilobyte",
                "Kibibyte",
                "Megabyte",
                "Mebibyte",
                "Gigabyte",
                "Gibibyte",
                "Terabyte",
                "Tebibyte",
                "Petabyte",
                "Pebibyte"
            )
        )
        data.fromMain =
            doubleArrayOf(
                8000000.0,
                8000.0,
                7812.5,
                8.0,
                7.62939,
                0.008,
                1 / 134.0,
                0.000008,
                1 / 137439.0,
                0.000000008,
                1000000 / 140.73748836,
                1000000.0,
                1000.0,
                976.563,
                1.0,
                0.953674,
                0.001,
                0.000931323,
                0.000001,
                0.000000909495,
                0.000000001,
                0.00000000088817842
            )

        data.toMain = (calculateToMain(data))
        return data
    }

    fun getTime(): Measure {
        time = Measure("Time")
        time.units = (
            arrayOf(
                "Nanosecond",
                "Microsecond",
                "Millisecond",
                "Second",
                "Minute",
                "Hour",
                "Day",
                "Week",
                "Month",
                "Calendar Year",
                "Decade",
                "Century"
            )
        )
        time.fromMain =
            doubleArrayOf(
                1000000000.0,
                1000000.0,
                1000.0,
                1 / 60.0,
                1 / 3600.0,
                1 / 86400.0,
                1 / 604800.0,
                1 / (30.4167 * 1 / 86400),
                1 / 3153600.0,
                1 / 31536000.0,
                1 / 315360000.0
            )

        time.toMain = (calculateToMain(time))
        return time
    }

    fun getVolume(): Measure {
        volume = Measure("Volume")
        volume.units = (
            arrayOf(
                "US liquid gallon",
                "US liquid quart",
                "US liquid pint",
                "US legal cup",
                "fluid ounce",
                "US tablespoon",
                "US teaspoon",
                "Cubic meter",
                "Liter",
                "Milliliter",
                "Imperial gallon",
                "imp. quart",
                "Imperial pint",
                "Imperial cup",
                "fluid ounce",
                "Imperial tablespoon",
                "Imperial teaspoon",
                "Cubic foot",
                "Cubic inch"
            )
        )
        volume.fromMain =
            doubleArrayOf(
                264.172,
                1056.69,
                1 / 0.00024,
                33814.0,
                67628.0,
                202884.0,
                1.0,
                1000.0,
                1000000.0,
                219.969,
                879.877,
                1759.75,
                3519.51,
                35195.1,
                56312.1,
                168936.0,
                35.3147,
                61023.7
            )

        volume.toMain = (calculateToMain(volume))
        return volume
    }

    fun getTemperature(): Measure {
        temperature = Measure("Temperature")
        temperature.units = (arrayOf("Celsius", "Fahrenheit", "Kelvin"))
        temperature.fromMain = doubleArrayOf()
        temperature.toMain = (calculateToMain(temperature))
        return temperature
    }

    fun getCurrency(): Measure {
        val rates: Rates = StartUp.currentRates!!
        currency = Measure("Currency")
        currency.units = (
            arrayOf(
                "AUD - Australian Dollar",
                "BGN - Bulgarian Lev",
                "BRL - Brazilian Rea",
                "CAD - Canadian Dollar",
                "CHF - Swiss Franc",
                "CNY - Chinese Yuan",
                "CZK - Czech Koruna",
                "DKK - Danish Krone",
                "GBP - British Pound",
                "HKD - Hong Kong Dollar",
                "HRK - Croatian Kuna",
                "HUF - Hungarian Forint",
                "IDR - Indonesian Rupiah",
                "ILS - Israeli New Shekel",
                "INR - Indian Rupee",
                "ISK - Icelandic Króna",
                "JPY - Japanese Yen",
                "KRW - South Korean Won",
                "MXN - Mexican Peso",
                "MYR - Malaysian Ringgit",
                "NOK - Norwegian Krone",
                "NZD - New Zealand Dollar",
                "PHP - Philippine Peso",
                "PLN - Polish Zloty",
                "RON - Romanian Leu",
                "RUB - Russian Ruble",
                "SEK - Swedish Krona",
                "SGD - Singapore Dollar",
                "THB - Thai Baht",
                "TRY - Turkish Lira",
                "USD - Us Dollar",
                "ZAR - South African Rand"
            )
        )
        currency.fromMain =
            doubleArrayOf(
                rates.AUD,
                rates.BGN,
                rates.BRL,
                rates.CAD,
                rates.CHF,
                rates.CNY,
                rates.CZK,
                rates.DKK,
                rates.GBP,
                rates.HKD,
                rates.HRK,
                rates.HUF,
                rates.IDR,
                rates.ILS,
                rates.INR,
                rates.ISK,
                rates.JPY,
                rates.KRW,
                rates.MXN,
                rates.MYR,
                rates.NOK,
                rates.NZD,
                rates.PHP,
                rates.PLN,
                rates.RON,
                rates.RUB,
                rates.SEK,
                rates.SGD,
                rates.THB,
                rates.TRY,
                rates.USD.toDouble(),
                rates.ZAR
            )

        currency.toMain = (calculateToMain(currency))
        return currency
    }

    private fun calculateToMain(measure: Measure): DoubleArray {
        val toMain = DoubleArray(measure.fromMain.size)
        for (i in measure.fromMain.indices) {
            toMain[i] = 1 / measure.fromMain[i]
        }
        return toMain
    }

}