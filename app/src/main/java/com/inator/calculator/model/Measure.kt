package com.inator.calculator.model

class Measure(var name: String) {


    //value To Convert To Main Unit (Metre, Kilogram, etc.) //So first we convert to these main units then
    lateinit var toMain: DoubleArray

    //value To Convert From Main Unit  // We convert them to required unit by multiplying with this value
    lateinit var fromMain: DoubleArray


}