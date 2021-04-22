package com.inator.calculator.fragments

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.inator.calculator.R
import com.inator.calculator.repository.CalculatorOld
import com.inator.calculator.viewmodel.CalculatorInputViewModel
import com.inator.calculator.viewmodel.HistoryViewModel
import com.inator.calculator.views.DraggablePanel
import kotlinx.android.synthetic.main.fragment_calculator.*
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.layout_adv_calculator.*
import kotlinx.android.synthetic.main.layout_app_bar_history.*
import kotlinx.android.synthetic.main.layout_input_field.*
import kotlinx.android.synthetic.main.layout_simple_calc.*
import java.util.*


class CalculatorFragment : Fragment(), View.OnClickListener {


    private val historyViewModel: HistoryViewModel by viewModels()
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
            draggablePanel.smoothPanelClose(200)
            input.setText(it.expr)
            output.text = it.answer
        })
        calcViewModel.inputLiveData.observe(viewLifecycleOwner, {
            input.setText(it)
            Log.i(input.selectionStart.toString(), input.selectionEnd.toString())
            calcViewModel.calculateOutput()
        })
        calcViewModel.outputLiveData.observe(viewLifecycleOwner, {
            output.text = it
        })
        calcViewModel.cursorLiveData.observe(viewLifecycleOwner, {
            input.setSelection(it)
        })
        calcViewModel.isDegreeLiveData.observe(viewLifecycleOwner, {
            if (it) angleButton.text =
                resources.getString(R.string.radian) else angleButton.text =
                resources.getString(R.string.degree)
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


    private fun setUpViews() {
        slidingPaneLayout.openPane()
        val color = ContextCompat.getColor(requireContext(), android.R.color.transparent)
        slidingPaneLayout.sliderFadeColor = color

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
            calcViewModel.otherClicked(
                (it as Button).text
            )
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
            calcViewModel.angleClicked()
        }
        equalButton.setOnClickListener {
            calcViewModel.equalClicked()
        }
        clearButton.setOnClickListener {
            val position = input.selectionStart
            if (!(input.text.toString().isEmpty() || input.selectionStart == 0)) {
                input.text?.delete(position - 1, position)
            }
            calcViewModel.backspaceClicked(input.selectionStart, input.selectionEnd)
            Log.i(input.selectionStart.toString(), input.selectionEnd.toString())
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
        /*     ***     Visual Stuff     ***     */
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
                if (input.text.isNullOrEmpty()) {
                    inputField.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        0f + 0.3f * (1 - mDragOffset)
                    )
                } else {
                    inputField.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        0.3f + 0.3f * (1 - mDragOffset)
                    )
                }
            }

            override fun onPanelOpened(view: View) {
                val currentWeight = (inputField.layoutParams as LinearLayout.LayoutParams).weight


                val animator: ValueAnimator
                if (input.text.isNullOrEmpty()) {
                    animator = ValueAnimator.ofFloat(currentWeight, 0.0f)
                } else {
                    animator = ValueAnimator.ofFloat(currentWeight, 0.3f)
                    header.visibility = View.VISIBLE
                }
                animator.addUpdateListener {
                    inputField.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            0,
                            it.animatedValue as Float
                        )
                }
                animator.duration = 300
                animator.interpolator = DecelerateInterpolator()
                animator.start()
                historyViewModel.setHistoryOpen(true)
            }

            override fun onPanelClosed(view: View) {
                val currentWeightInput =
                    (inputField.layoutParams as LinearLayout.LayoutParams).weight
                val currentWeightHistory =
                    (historyContainer.layoutParams as LinearLayout.LayoutParams).weight

                val inputAnimator = ValueAnimator.ofFloat(currentWeightInput, 1.0f)
                val historyAnimator = ValueAnimator.ofFloat(currentWeightHistory, 0.0f)
                inputAnimator.addUpdateListener {
                    inputField.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            0,
                            it.animatedValue as Float
                        )

                }
                historyAnimator.addUpdateListener {
                    historyContainer.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            0,
                            it.animatedValue as Float
                        )

                }
                inputAnimator.duration = 300
                historyAnimator.duration = 300
                inputAnimator.interpolator = DecelerateInterpolator()
                historyAnimator.interpolator = DecelerateInterpolator()
                inputAnimator.start()
                historyAnimator.start()
                header.visibility = View.GONE
                historyViewModel.setHistoryOpen(false)
            }
        })
    }


    override fun onClick(v: View) {
        if (input.text.toString() == "Not a Number") {
            input.setText("")
        }
        val buttonText = (v as Button).text as String
        val currentPosition = input.selectionStart
        val oldInput = input.text.toString()
        val validateInput: BooleanArray =
            CalculatorOld.validateInput(currentPosition, buttonText, oldInput)
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

}




