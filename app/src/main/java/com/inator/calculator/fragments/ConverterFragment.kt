package com.inator.calculator.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inator.calculator.R
import com.inator.calculator.databinding.FragmentConverterBinding
import com.inator.calculator.viewmodel.ConverterInputViewModel

class ConverterFragment : Fragment(R.layout.fragment_converter) {
    private lateinit var textWatcher1: TextWatcher
    private lateinit var textWatcher2: TextWatcher
    private val converterInputViewModel: ConverterInputViewModel by viewModels()
    private var hasSetSavedSpinners = false

    private var _binding: FragmentConverterBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentConverterBinding.bind(view)
        setUpViews()
    }

    private fun setUpViews() {
        binding.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
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
        binding.chipGroup.check(converterInputViewModel.getSavedMeasure())
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
        binding.editText1.addTextChangedListener(textWatcher1)
        binding.editText2.addTextChangedListener(textWatcher2)
        binding.unit1.onItemSelectedListener = object : OnItemSelectedListener {
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
        binding.unit2.onItemSelectedListener = object : OnItemSelectedListener {
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
            binding.editText2.apply {
                removeTextChangedListener(textWatcher2)
                setText(it)
                addTextChangedListener(textWatcher2)
            }
        }
        converterInputViewModel.getOutputReverse().observe(viewLifecycleOwner) {
            binding.editText1.apply {
                removeTextChangedListener(textWatcher2)
                setText(it)
                addTextChangedListener(textWatcher2)
            }
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
            binding.unit1.adapter = unitAdapter
            binding.unit2.adapter = unitAdapter
            setSavedSpinners()

        }
    }

    private fun setSavedSpinners() {
        if (!hasSetSavedSpinners) {
            binding.unit1.setSelection(converterInputViewModel.getSavedSpinner1())
            binding.unit2.setSelection(converterInputViewModel.getSavedSpinner2())
            hasSetSavedSpinners = true
        } else {
            binding.unit1.setSelection(0)
            binding.unit2.setSelection(1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}