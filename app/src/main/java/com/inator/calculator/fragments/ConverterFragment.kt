package com.inator.calculator.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.calculator.inator.R
import com.google.android.material.chip.ChipGroup
import com.inator.calculator.Data.ConvertData
import com.inator.calculator.Model.Measure
import kotlinx.android.synthetic.main.fragment_converter.*

class ConverterFragment : Fragment(), OnItemSelectedListener {
    lateinit var textWatcher1: TextWatcher
    lateinit var textWatcher2: TextWatcher
    private var unitAdapter: ArrayAdapter<String?>? = null
    var currentMeasure: Measure? = ConvertData.getLength()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        currentMeasure = ConvertData.getLength()

        return inflater.inflate(R.layout.fragment_converter, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpSpinners()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setUpSpinners() {
        chipGroup.setOnCheckedChangeListener { _: ChipGroup?, checkedId: Int ->
            when (checkedId) {
                R.id.length -> {
                    currentMeasure = ConvertData.getLength()
                }
                R.id.mass -> {
                    currentMeasure = ConvertData.getMass()
                }
                R.id.area -> {
                    currentMeasure = ConvertData.getArea()
                }
                R.id.speed -> {
                    currentMeasure = ConvertData.getSpeed()
                }
                R.id.angle -> {
                    currentMeasure = ConvertData.getAngle()
                }
                R.id.data -> {
                    currentMeasure = ConvertData.getData()
                }
                R.id.time -> {
                    currentMeasure = ConvertData.getTime()
                }
                R.id.volume -> {
                    currentMeasure = ConvertData.getVolume()
                }
                R.id.temperature -> {
                    currentMeasure = ConvertData.getTemperature()
                }
                R.id.currency -> {
                    try {
                        currentMeasure = ConvertData.getCurrency()
                    } catch (e: NullPointerException) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to get Currency Rates! Please check your Internet Connection ;(",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            setUpSpinnerAdapter()
            textWatcher1.afterTextChanged(editText1.text)
        }
        setUpSpinnerAdapter()
        textWatcher1 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val toMain: Double
                val fromMain: Double
                if (s.toString().isEmpty()) {
                    editText2.removeTextChangedListener(textWatcher2)
                    editText2.setText("0")
                    editText2.addTextChangedListener(textWatcher2)
                    return
                }
                val input = s.toString().toDouble()
                val selectedOne = unit1.selectedItemPosition
                val selectedTwo = unit2.selectedItemPosition
                if (currentMeasure?.name == "Temperature") {
                    toMain = when {
                        unit1.selectedItem.toString() == "Celsius" -> {
                            input
                        }
                        unit1.selectedItem.toString() == "Fahrenheit" -> {
                            (input - 32) * (5 / 9.0)
                        }
                        else -> {
                            input - 273.15
                        }
                    }
                    fromMain = when {
                        unit2.selectedItem.toString() == "Celsius" -> {
                            toMain
                        }
                        unit2.selectedItem.toString() == "Fahrenheit" -> {
                            toMain * (9 / 5.0) + 32
                        }
                        else -> {
                            toMain + 273.15
                        }
                    }
                } else {
                    toMain = input * currentMeasure!!.toMain[selectedOne]
                    fromMain = toMain * currentMeasure!!.fromMain[selectedTwo]
                }
                editText2.removeTextChangedListener(textWatcher2)
                val result = fromMain.toString()
                if (fromMain.toString().endsWith(".0")) {
                    editText2.setText(result.substring(0, result.length - 2))
                } else {
                    editText2.setText(fromMain.toString())
                }
                editText2.addTextChangedListener(textWatcher2)
            }
        }
        textWatcher2 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val toMain: Double
                val fromMain: Double
                if (s.toString().isEmpty()) {
                    editText1.removeTextChangedListener(textWatcher1)
                    editText1.setText("0")
                    editText1.addTextChangedListener(textWatcher1)
                    return
                }
                val input = s.toString().toDouble()
                val selectedOne = unit1.selectedItemPosition
                val selectedTwo = unit2.selectedItemPosition
                if (currentMeasure?.name == "Temperature") {
                    toMain = when {
                        unit2.selectedItem.toString() == "Celsius" -> {
                            input
                        }
                        unit2.selectedItem.toString() == "Fahrenheit" -> {
                            (input - 32) * (5 / 9.0)
                        }
                        else -> {
                            input - 273.15
                        }
                    }
                    fromMain = when {
                        unit1.selectedItem.toString() == "Celsius" -> {
                            toMain
                        }
                        unit1.selectedItem.toString() == "Fahrenheit" -> {
                            toMain * (9 / 5.0) + 32
                        }
                        else -> {
                            toMain + 273.15
                        }
                    }
                } else {
                    toMain = input * currentMeasure!!.toMain[selectedTwo]
                    fromMain = toMain * currentMeasure!!.fromMain[selectedOne]
                }
                editText1.removeTextChangedListener(textWatcher1)
                val result = fromMain.toString()
                if (fromMain.toString().endsWith(".0")) {
                    editText1.setText(result.substring(0, result.length - 2))
                } else {
                    editText1.setText(fromMain.toString())
                }
                editText1.addTextChangedListener(textWatcher1)
            }
        }
        editText1.addTextChangedListener(textWatcher1)
        editText2.addTextChangedListener(textWatcher2)
        unit1.onItemSelectedListener = this
        unit2.onItemSelectedListener = this
    }

    private fun setUpSpinnerAdapter() {
        unitAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, currentMeasure!!.units) }
        unitAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        unit1.adapter = unitAdapter
        unit2.adapter = unitAdapter
        unit2.setSelection(1)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        textWatcher1.afterTextChanged(editText1.text)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}