package com.inator.calculator.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.inator.calculator.R
import com.inator.calculator.viewmodel.CalculatorInputViewModel
import com.inator.calculator.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_calculator.*
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.layout_adv_calculator.*
import kotlinx.android.synthetic.main.layout_input_field.*
import kotlinx.android.synthetic.main.layout_simple_calc.*
import org.mariuszgromada.math.mxparser.mXparser
import java.util.*


class CalculatorFragment : Fragment() {
    private val historyViewModel: HistoryViewModel by activityViewModels()
    private val calcViewModel: CalculatorInputViewModel by viewModels()

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
        addObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun addObservers() {
        historyViewModel.clickedHistory.observe(viewLifecycleOwner, {
            draggablePanel.smoothPanelClose(300)
            input.setText(it.expr)
            output.text = it.answer
        })
        calcViewModel.inputLiveData.observe(viewLifecycleOwner, {
            input.setText(it)
            calcViewModel.calculateOutput()
        })
        calcViewModel.outputLiveData.observe(viewLifecycleOwner, {
            output.text = it
        })
        calcViewModel.cursorLiveData.observe(viewLifecycleOwner, {
            input.setSelection(it)
        })
        calcViewModel.isInverseLiveData.observe(viewLifecycleOwner, {
            if (it) {
                val color = ContextCompat.getColor(requireContext(), R.color.grey)
                inverseButton.setBackgroundColor(color)
                sinButton.setText(R.string.asin)
                cosButton.setText(R.string.acos)
                tanButton.setText(R.string.atan)
                rootButton.setText(R.string.square)
                naturalLogButton.setText(R.string.eRaisedTo)
                log10Button.setText(R.string.tenRaisedTo)
            } else {
                val color = ContextCompat.getColor(requireContext(), android.R.color.transparent)
                inverseButton.setBackgroundColor(color)
                sinButton.setText(R.string.sin)
                cosButton.setText(R.string.cos)
                tanButton.setText(R.string.tan)
                rootButton.setText(R.string.root)
                naturalLogButton.setText(R.string.natural_log)
                log10Button.setText(R.string.log)
            }
        })

    }


    @SuppressLint("RestrictedApi")
    private fun setUpViews() {
        slidingPaneLayout.openPane()
        val color = ContextCompat.getColor(requireContext(), android.R.color.transparent)
        slidingPaneLayout.sliderFadeColor = color

        //Setting AutoResize For TextView
        output.setAutoSizeTextTypeUniformWithConfiguration(10, 24, 1, TypedValue.COMPLEX_UNIT_SP)

        // Don't show keyboard on focus/click
        input.showSoftInputOnFocus = false
        input.requestFocus()
    }

    private fun addListeners() {
        // Adding OnClick Listener+
        //Handling Numbers
        button0.setOnClickListener {
            calcViewModel.numClicked('0')
        }
        button1.setOnClickListener {
            calcViewModel.numClicked('1')
        }
        button2.setOnClickListener {
            calcViewModel.numClicked('2')
        }
        button3.setOnClickListener {
            calcViewModel.numClicked('3')
        }
        button4.setOnClickListener {
            calcViewModel.numClicked('4')
        }
        button5.setOnClickListener {
            calcViewModel.numClicked('5')
        }
        button6.setOnClickListener {
            calcViewModel.numClicked('6')
        }
        button7.setOnClickListener {
            calcViewModel.numClicked('7')
        }
        button8.setOnClickListener {
            calcViewModel.numClicked('8')
        }
        button9.setOnClickListener {
            calcViewModel.numClicked('9')
        }
        //They are numbers after all
        piButton.setOnClickListener {
            calcViewModel.numClicked('π')
        }
        eulerNumButton.setOnClickListener {
            calcViewModel.numClicked('e')
        }


        //Handling Operations
        addButton.setOnClickListener {
            calcViewModel.operClicked('+')
        }
        subButton.setOnClickListener {
            calcViewModel.operClicked('-')
        }
        multButton.setOnClickListener {
            calcViewModel.operClicked('×')
        }
        divideButton.setOnClickListener {
            calcViewModel.operClicked('÷')
        }
        percentButton.setOnClickListener {
            calcViewModel.operClicked('%')
        }
        powerButton.setOnClickListener {
            calcViewModel.operClicked('^')
        }

        //Handling Decimal
        decimalButton.setOnClickListener {
            calcViewModel.decimalClicked()
        }

        //Handling Functions
        sinButton.setOnClickListener {
            calcViewModel.funClicked((it as Button).text)
        }
        cosButton.setOnClickListener {
            calcViewModel.funClicked((it as Button).text)
        }
        tanButton.setOnClickListener {
            calcViewModel.funClicked((it as Button).text)
        }
        naturalLogButton.setOnClickListener {
            calcViewModel.funClicked((it as Button).text)
        }
        log10Button.setOnClickListener {
            calcViewModel.funClicked((it as Button).text)
        }
        rootButton.setOnClickListener {
            if ((it as Button).text.toString() == "√") {
                calcViewModel.funClicked("sqrt")
            } else {
                calcViewModel.otherClicked(it.text)
            }
        }

        openBracketButton.setOnClickListener {
            calcViewModel.otherClicked('(')
        }
        closeBracketButton.setOnClickListener {
            calcViewModel.otherClicked(')')
        }
        factorialButton.setOnClickListener {
            calcViewModel.otherClicked('!')
        }
        inverseButton.setOnClickListener {
            calcViewModel.inverseClicked()
        }
        angleButton.setOnClickListener {
            if (mXparser.checkIfRadiansMode()) {
                mXparser.setDegreesMode()
                angleButton.text = resources.getString(R.string.radian)

            } else {
                mXparser.setRadiansMode()
                angleButton.text =
                        resources.getString(R.string.degree)
            }

        }
        equalButton.setOnClickListener {
            calcViewModel.equalClicked(requireContext())
        }
        clearButton.setOnClickListener {
            input.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
            calcViewModel.backspaceClicked(input.text.toString(), input.selectionEnd)
        }

        clearButton.setOnLongClickListener {
            calcViewModel.clearAll()
            return@setOnLongClickListener true
        }

        input.setOnClickListener {
            calcViewModel.setCursor(input.selectionStart)
        }
        input.setOnLongClickListener {
            calcViewModel.setCursor(input.selectionStart)
            return@setOnLongClickListener false
        }
        /*      Visual Stuff       */
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
    }

    override fun onResume() {
        super.onResume()
        val imm: InputMethodManager? = getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}