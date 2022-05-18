package com.inator.calculator.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inator.calculator.R
import com.inator.calculator.adapters.SpinnerAdapter
import com.inator.calculator.databinding.FragmentCurrencyBinding
import com.inator.calculator.extensions.isLandscape
import com.inator.calculator.viewmodel.CurrencyInputViewModel
import com.inator.calculator.viewmodel.ExchangeRatesViewModel
import java.text.SimpleDateFormat
import java.util.*


class CurrencyFragment : Fragment(R.layout.fragment_currency) {
    private val exchangeRatesViewModel: ExchangeRatesViewModel by viewModels()
    private val currencyInputViewModel: CurrencyInputViewModel by viewModels()

    private var _binding: FragmentCurrencyBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCurrencyBinding.bind(view)

        val sharedPrefs = context?.getSharedPreferences("Exchange-Rates", Context.MODE_PRIVATE)
        val todayDate = Calendar.getInstance().time
        val lastUpdatedDateString = sharedPrefs?.getString("_date", "null")

        if (lastUpdatedDateString == "null") {
            getExchangeRates()
        } else {
            val lastUpdatedDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(lastUpdatedDateString!!)
            if (todayDate.time - lastUpdatedDate!!.time < MILLIS_PER_DAY) getExchangeRates()
        }

        exchangeRatesViewModel.isFetching(requireContext())
            .observe(viewLifecycleOwner) {
                binding.progressBar.isVisible = it
            }

        exchangeRatesViewModel.getExchangeRates(requireContext()).observe(
            viewLifecycleOwner
        ) {
            if (it?.rates != null) {
                binding.currencySpinner1.adapter = SpinnerAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    it.rates
                )
                binding.currencySpinner2.adapter = SpinnerAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    it.rates
                )
                //Getting Last States
                binding.currencySpinner1.setSelection(
                    (binding.currencySpinner1.adapter as SpinnerAdapter).getPosition(
                        currencyInputViewModel.getSavedSpinner1()!!
                    )
                )
                binding.currencySpinner2.setSelection(
                    (binding.currencySpinner2.adapter as SpinnerAdapter).getPosition(
                        currencyInputViewModel.getSavedSpinner2()!!
                    )
                )
            }
        }

        setUpViews()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setUpViews() {
        binding.change.setOnClickListener {
            binding.run {
                val otherSelected = currencySpinner1.selectedItemPosition
                if (!requireContext().isLandscape) {
                    val moveUpAnimation = AnimationUtils.loadAnimation(context, R.anim.move_up)
                    val moveDownAnimation = AnimationUtils.loadAnimation(context, R.anim.move_down)
                    currencySpinner1.startAnimation(moveDownAnimation)
                    currencySpinner2.startAnimation(moveUpAnimation)
                    currencySpinner1.startAnimation(moveUpAnimation)
                    currencySpinner2.startAnimation(moveDownAnimation)
                }
                currencySpinner1.setSelection(currencySpinner2.selectedItemPosition)
                currencySpinner2.setSelection(otherSelected)
            }
        }
        binding.refreshButton.setOnClickListener {
            getExchangeRates()
        }

        binding.currencySpinner1.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currencyInputViewModel.setSpinner1(
                        (parent?.adapter as SpinnerAdapter).getItem(
                            position
                        )
                    )
                }
            }

        binding.currencySpinner2.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currencyInputViewModel.setSpinner2(
                        (parent?.adapter as SpinnerAdapter).getItem(
                            position
                        )
                    )
                }
            }

        val textWatcher1 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                currencyInputViewModel.setInput1(s.toString())
            }
        }
        val textWatcher2 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                currencyInputViewModel.setInput2(s.toString())
            }

        }
        binding.input1.addTextChangedListener(textWatcher1)
        binding.input2.addTextChangedListener(textWatcher2)

        currencyInputViewModel.getOutputDirect().observe(
            viewLifecycleOwner
        ) {
            binding.input2.apply {
                removeTextChangedListener(textWatcher2)
                setText(it)
                addTextChangedListener(textWatcher2)
            }
        }
        currencyInputViewModel.getOutputReverse().observe(
            viewLifecycleOwner
        ) {
            binding.input1.apply {
                removeTextChangedListener(textWatcher1)
                setText(it)
                addTextChangedListener(textWatcher1)
            }
        }
    }

    private fun getExchangeRates() {
        exchangeRatesViewModel.fetchExchangeRates(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MILLIS_PER_DAY = 86400000
    }
}
