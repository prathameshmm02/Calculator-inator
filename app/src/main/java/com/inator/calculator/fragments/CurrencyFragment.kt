package com.inator.calculator.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inator.calculator.R
import com.inator.calculator.repository.Data
import com.inator.calculator.viewmodel.ExchangeRatesViewModel
import kotlinx.android.synthetic.main.fragment_currency.*


class CurrencyFragment : Fragment() {
    private val exchangeRatesViewModel: ExchangeRatesViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        context?.let { Data.getInstance(it).fetchExchangeRates() }

        context?.let { context ->
            exchangeRatesViewModel.getExchangeRates(context)
                .observe(viewLifecycleOwner, { currency ->
                    currency.rates?.forEach {
                        Log.i(it.code, it.value.toString())
                    }
                })
        }
        return inflater.inflate(R.layout.fragment_currency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViews()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setUpViews(){
        fab.setOnClickListener {
            val otherSelected = currencySpinner1.selectedItemPosition
            currencySpinner1.setSelection(currencySpinner2.selectedItemPosition)
            currencySpinner2.setSelection(otherSelected)
        }
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.currency,
                android.R.layout.simple_spinner_item
            ).also { unitAdapter ->
                unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                currencySpinner1.adapter = unitAdapter
                currencySpinner2.adapter = unitAdapter
                currencySpinner2.setSelection(1)
            }
        }
    }
}
