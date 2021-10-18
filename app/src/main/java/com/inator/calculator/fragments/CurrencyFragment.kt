package com.inator.calculator.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inator.calculator.R
import com.inator.calculator.adapters.SpinnerAdapter
import com.inator.calculator.viewmodel.CurrencyInputViewModel
import com.inator.calculator.viewmodel.ExchangeRatesViewModel
import kotlinx.android.synthetic.main.fragment_currency.*
import java.text.SimpleDateFormat
import java.util.*


class CurrencyFragment : Fragment() {
    private val exchangeRatesViewModel: ExchangeRatesViewModel by viewModels()
    private val currencyInputViewModel: CurrencyInputViewModel by viewModels()
    private val MILLIS_PER_DAY = 86400000


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
            .observe(viewLifecycleOwner, {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                }
            })

        exchangeRatesViewModel.getExchangeRates(requireContext()).observe(
            viewLifecycleOwner, {
                if (it?.rates != null) {
                    currencySpinner1.adapter = SpinnerAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        it.rates
                    )
                    currencySpinner2.adapter = SpinnerAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        it.rates
                    )
                    //Getting Last States
                    currencySpinner1.setSelection(
                        (currencySpinner1.adapter as SpinnerAdapter).getPosition(
                            currencyInputViewModel.getSavedSpinner1()!!
                        )
                    )
                    currencySpinner2.setSelection(
                        (currencySpinner2.adapter as SpinnerAdapter).getPosition(
                            currencyInputViewModel.getSavedSpinner2()!!
                        )
                    )
                }
            }
        )

        setUpViews()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setUpViews() {
        change.setOnClickListener {
            val otherSelected = currencySpinner1.selectedItemPosition
            val moveUpAnimation = AnimationUtils.loadAnimation(context, R.anim.move_up)
            val moveDownAnimation = AnimationUtils.loadAnimation(context, R.anim.move_down)
            currencySpinner1.startAnimation(moveDownAnimation)
            currencySpinner2.startAnimation(moveUpAnimation)
            currencySpinner1.setSelection(currencySpinner2.selectedItemPosition)
            currencySpinner2.setSelection(otherSelected)
            currencySpinner1.startAnimation(moveUpAnimation)
            currencySpinner2.startAnimation(moveDownAnimation)
        }
        refreshButton.setOnClickListener {
            getExchangeRates()
        }


        currencySpinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        currencySpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        input1.addTextChangedListener(textWatcher1)
        input2.addTextChangedListener(textWatcher2)

        currencyInputViewModel.getOutputDirect().observe(
            viewLifecycleOwner, {
                input2.removeTextChangedListener(textWatcher2)
                input2.setText(it)
                input2.addTextChangedListener(textWatcher2)
            }
        )
        currencyInputViewModel.getOutputReverse().observe(
            viewLifecycleOwner, {
                input1.removeTextChangedListener(textWatcher1)
                input1.setText(it)
                input1.addTextChangedListener(textWatcher1)
            }
        )
    }

    private fun getExchangeRates() {
        exchangeRatesViewModel.fetchExchangeRates(requireContext())
    }
}
