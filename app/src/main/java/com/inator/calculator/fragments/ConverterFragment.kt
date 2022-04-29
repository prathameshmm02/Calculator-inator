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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inator.calculator.R
import com.inator.calculator.viewmodel.ConverterInputViewModel
import kotlinx.android.synthetic.main.fragment_converter.*

class ConverterFragment : Fragment() {
    private lateinit var textWatcher1: TextWatcher
    private lateinit var textWatcher2: TextWatcher
    private val converterInputViewModel: ConverterInputViewModel by viewModels()
    private var hasSetSavedSpinners = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_converter, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViews()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setUpViews() {

        chipGroup.setOnCheckedStateChangeListener { _, checkedIds  ->
            when (checkedIds[0]) {
                R.id.length -> {
                    converterInputViewModel.setMeasure("Length")
                }
                R.id.mass -> {
                    converterInputViewModel.setMeasure("Mass")
                }
                R.id.area -> {
                    converterInputViewModel.setMeasure("Area")
                }
                R.id.speed -> {
                    converterInputViewModel.setMeasure("Speed")
                }
                R.id.angle -> {
                    converterInputViewModel.setMeasure("Angle")
                }
                R.id.data -> {
                    converterInputViewModel.setMeasure("Data")
                }
                R.id.time -> {
                    converterInputViewModel.setMeasure("Time")
                }
                R.id.volume -> {
                    converterInputViewModel.setMeasure("Volume")
                }
                R.id.temperature -> {
                    converterInputViewModel.setMeasure("Temperature")
                }
            }
        }
        chipGroup.check(converterInputViewModel.getSavedMeasure())
        textWatcher1 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                converterInputViewModel.setInput1(s.toString())
            }
        }
        textWatcher2 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                converterInputViewModel.setInput2(s.toString())
            }
        }
        editText1.addTextChangedListener(textWatcher1)
        editText2.addTextChangedListener(textWatcher2)
        unit1.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                converterInputViewModel.setSpinner1(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
        unit2.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                converterInputViewModel.setSpinner2(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



        converterInputViewModel.getOutputDirect().observe(viewLifecycleOwner) {
            editText2.removeTextChangedListener(textWatcher2)
            editText2.setText(it)
            editText2.addTextChangedListener(textWatcher2)

        }
        converterInputViewModel.getOutputReverse().observe(viewLifecycleOwner) {
            editText1.removeTextChangedListener(textWatcher1)
            editText1.setText(it)
            editText1.addTextChangedListener(textWatcher1)
        }
        converterInputViewModel.getMeasure().observe(
            viewLifecycleOwner
        ) {
            setUpSpinnerAdapter(it)
        }

    }


    private fun setUpSpinnerAdapter(string: String) {

        val unitArray = when (string) {
            "Length" -> R.array.length_units
            "Area" -> R.array.area_units
            "Mass" -> R.array.mass_units
            "Speed" -> R.array.speed_units
            "Data" -> R.array.storage_units
            "Volume" -> R.array.volume_units
            "Time" -> R.array.time_units
            "Temperature" -> R.array.temperature_units
            "Angle" -> R.array.angle_units
            else -> R.array.length_units
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            unitArray,
            android.R.layout.simple_spinner_item
        ).also { unitAdapter ->
            unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            unit1.adapter = unitAdapter
            unit2.adapter = unitAdapter
            setSavedSpinners()

        }
    }

    private fun setSavedSpinners() {
        if (!hasSetSavedSpinners) {
            unit1.setSelection(converterInputViewModel.getSavedSpinner1())
            unit2.setSelection(converterInputViewModel.getSavedSpinner2())
            hasSetSavedSpinners = true
        } else {
            unit1.setSelection(0)
            unit2.setSelection(1)
        }

    }


}