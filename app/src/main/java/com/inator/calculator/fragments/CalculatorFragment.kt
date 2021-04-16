package com.inator.calculator.fragments

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inator.calculator.History.History
import com.inator.calculator.viewmodel.HistoryViewModel
import com.inator.calculator.Model.Calculator
import com.inator.calculator.R
import com.inator.calculator.views.DraggablePanel
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.adv_calculator_layout.*
import kotlinx.android.synthetic.main.fragment_calculator.*
import kotlinx.android.synthetic.main.input_field.*
import kotlinx.android.synthetic.main.simple_calc_layout.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class CalculatorFragment : Fragment(), View.OnClickListener {

    val operators = listOf('^', '%', '÷', '×', '+', '-')
    private var isInvert = false
    var isDegree = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.historyContainer, HistoryFragment())
            .commit()

        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViews()
        addListeners()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpViews() {
        // This was the most boring part of my life ;)
        setBackground()
        slidingPaneLayout.openPane()
        context?.let {
            val color = ContextCompat.getColor(it, android.R.color.transparent)
            slidingPaneLayout.sliderFadeColor = color
        }

        // Don't show keyboard on focus/click
        input.showSoftInputOnFocus = false
        input.requestFocus()
    }

    private fun addListeners() {
        // Adding OnClick Listener
        button0.setOnClickListener(this)
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        addButton.setOnClickListener(this)
        subButton.setOnClickListener(this)
        multButton.setOnClickListener(this)
        divideButton.setOnClickListener(this)
        decimalButton.setOnClickListener(this)
        percentButton.setOnClickListener(this)
        sinButton.setOnClickListener(this)
        cosButton.setOnClickListener(this)
        tanButton.setOnClickListener(this)
        naturalLogButton.setOnClickListener(this)
        log10Button.setOnClickListener(this)
        rootButton.setOnClickListener(this)
        piButton.setOnClickListener(this)
        eulerNumButton.setOnClickListener(this)
        powerButton.setOnClickListener(this)
        openBracketButton.setOnClickListener(this)
        closeBracketButton.setOnClickListener(this)
        factorialButton.setOnClickListener(this)
        inverseButton.setOnClickListener {
            isInvert = !isInvert
            if (isInvert) {
                context?.let {
                    val color = ContextCompat.getColor(it, android.R.color.transparent)
                    inverseButton.setBackgroundColor(color)
                }

                sinButton.setText(R.string.sin)
                cosButton.setText(R.string.cos)
                tanButton.setText(R.string.tan)
                rootButton.setText(R.string.root)
                naturalLogButton.setText(R.string.natural_log)
                log10Button.setText(R.string.log)
            } else {
                sinButton.setText(R.string.asin)
                cosButton.setText(R.string.acos)
                tanButton.setText(R.string.atan)
                rootButton.setText(R.string.square)
                naturalLogButton.setText(R.string.eRaisedTo)
                log10Button.setText(R.string.tenRaisedTo)
                context?.let {
                    val color = ContextCompat.getColor(it, R.color.grey)
                    inverseButton.setBackgroundColor(color)
                }
            }
        }
        angleButton.setOnClickListener {
            if (isDegree) angleButton.text =
                resources.getString(R.string.radian) else angleButton.text =
                resources.getString(R.string.degree)
            isDegree = !isDegree
        }
        equalButton.setOnClickListener {
            val expr = input.text.toString()
            var result = BigDecimal.ZERO
            try {
                if (operators.contains(expr[expr.length - 1])) {
                    return@setOnClickListener
                }
                if (expr.isNotEmpty()) {
                    val calc = Calculator()
                    result = calc.eval(expr, isDegree)
                    input.setText(result.stripTrailingZeros().toPlainString())
                    input.setSelection(input.text.toString().length)
                }
                output.text = ""
                input.text?.length?.let { it1 -> input.setSelection(it1) }
            } catch (e: Exception) {
                input.setText(R.string.error_text)
            }
            saveToHistory(expr, result.toPlainString())
        }
        clearButton.setOnClickListener {
            val position = input.selectionStart
            if (!(input.text.toString().isEmpty() || input.selectionStart == 0)) {
                input.text?.delete(position - 1, position)
            }
        }
        clearButton.setOnLongClickListener {
            input.setText("")
            output.text = ""
            true
        }
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val expr = s.toString()
                try {
                    if (expr.isEmpty()) {
                        output.text = ""
                    } else if (!operators.contains(expr[expr.length - 1])) {
                        val calc = Calculator()
                        val result = calc.eval(expr, isDegree)
                        output.text = result.stripTrailingZeros().toPlainString()
                    }
                } catch (e: Exception) {
                    output.text = ""
                }
            }
        })


        slideButton.setOnClickListener {
            if (slidingPaneLayout.isOpen) {
                slidingPaneLayout.closePane()
            } else {
                slidingPaneLayout.openPane()
            }
        }


        slidingPaneLayout.setPanelSlideListener(object : SlidingPaneLayout.PanelSlideListener {
            val move = AnimationUtils.loadAnimation(context, R.anim.move)
            val rotateFirst = AnimationUtils.loadAnimation(context, R.anim.rotate_first)
            override fun onPanelSlide(panel: View, slideOffset: Float) {
                slideButton.startAnimation(move)
            }

            override fun onPanelOpened(panel: View) {
                slideButton.clearAnimation()
            }

            override fun onPanelClosed(panel: View) {
                slideButton.clearAnimation()
                slideButton.startAnimation(rotateFirst)
            }
        })


        draggablePanel.setPanelSlideListener(object : DraggablePanel.PanelSlideListener {
            override fun onPanelSlide(view: View, mDragOffset: Float) {
                historyContainer.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    0,
                    1 * mDragOffset
                )
                inputField.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    0,
                    0.3f + 0.3f * (1 - mDragOffset)
                )

            }

            override fun onPanelOpened(view: View) {
                if (input.text.isNullOrEmpty()) {

                    inputField.layoutParams =
                        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0f)
                } else {
                    header.visibility = View.VISIBLE
                    inputField.layoutParams =
                        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.3f)

                }
            }

            override fun onPanelClosed(view: View) {
                historyContainer?.layoutParams =
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0F)
                view.findViewById<View>(R.id.inputField).layoutParams =
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1F)
                header.visibility = View.GONE

            }
        })
    }

    private fun saveToHistory(expression: String, answer: String) {
        val calendar = Calendar.getInstance()
        val date = SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(calendar.time)
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(calendar.time)
        val history = History(expression, answer, date, time)
        val viewModel : HistoryViewModel by viewModels()
        viewModel.insertHistory(history)
    }

    override fun onClick(v: View) {
        if (input.text.toString() == "Not a Number") {
            input.setText("")
        }
        val buttonText = (v as Button).text as String
        val currentPosition = input.selectionStart
        val oldInput = input.text.toString()
        val validateInput: BooleanArray =
            Calculator.validateInput(currentPosition, buttonText, oldInput)
        if (validateInput[0] && validateInput[1]) {
            val buttonText2 = "$buttonText()"
            val output = oldInput.substring(0, currentPosition) + buttonText2 + oldInput.substring(
                currentPosition
            )
            input.setText(output)
            input.setSelection(buttonText2.length + currentPosition - 1)
        } else if (validateInput[0]) {
            val output = oldInput.substring(0, currentPosition) + buttonText + oldInput.substring(
                currentPosition
            )
            input.setText(output)
            input.setSelection(buttonText.length + currentPosition)
        }
    }

    private fun setBackground() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val path = prefs.getString("background", "null")
        val blurRadius = prefs.getInt("blur", 0)
        if (prefs.getBoolean("customBG", false) && path != "null") {
            if (blurRadius != 0) {
                backgroundImage?.let {
                    Glide.with(this)
                        .load(Uri.parse(path))
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(blurRadius)))
                        .into(it)
                }
            } else {
                backgroundImage?.let {
                    Glide.with(this)
                        .load(Uri.parse(path))
                        .into(it)
                }
            }
        }
    }

    override fun onStart() {
        setBackground()
        super.onStart()
    }
//
//    fun setInputText(expression: String?) {
//        input.setText(expression)
//    }

}