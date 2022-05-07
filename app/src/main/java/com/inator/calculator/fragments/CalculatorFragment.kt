package com.inator.calculator.fragments

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.core.content.getSystemService
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.google.android.material.color.MaterialColors
import com.inator.calculator.R
import com.inator.calculator.activities.MainActivity
import com.inator.calculator.databinding.FragmentCalculatorBinding
import com.inator.calculator.extensions.*
import com.inator.calculator.viewmodel.CalculatorInputViewModel
import com.inator.calculator.viewmodel.HistoryViewModel
import com.inator.calculator.views.DraggablePanel
import org.mariuszgromada.math.mxparser.mXparser


class CalculatorFragment : Fragment() {
    private val historyViewModel: HistoryViewModel by activityViewModels()
    private val calcViewModel: CalculatorInputViewModel by viewModels()

    private val mainActivity get() = activity as MainActivity

    private val historyFragment = HistoryFragment()

    private var _binding: Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        childFragmentManager.beginTransaction()
            .replace(R.id.historyContainer, historyFragment)
            .commit()
        _binding = Binding(FragmentCalculatorBinding.inflate(layoutInflater, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            setUpViews()
            addListeners()
            addObservers()
            setupHistoryPanel()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) {
            if (binding.draggablePanel.isOpen()) {
                binding.draggablePanel.smoothPanelClose(300)
                remove()
            } else if (binding.slidingPaneLayout.isOpen) {
                binding.slidingPaneLayout.closePane()
                remove()
            }
        }
        mainActivity.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == android.R.id.home) {
                    binding.draggablePanel.smoothPanelClose(300)
                    return true
                }
                return false
            }
        })
    }

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.findItem(R.id.delete_history).apply{
                isVisible = true
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            }
            menu.findItem(R.id.action_settings).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }

        override fun onMenuItemSelected(menuItem: MenuItem) = false
    }

    private fun Binding.setupHistoryPanel() {
        draggablePanel.setPanelSlideListener(object : DraggablePanel.PanelSlideListener {
            override fun onPanelSlide(view: View, mDragOffset: Float) {
                if (input.text.isNullOrEmpty()) {
                    inputField.updateWeight(0f + 0.3f * (1 - mDragOffset))
                    historyContainer.updateWeight(1.0f * mDragOffset)
                } else {
                    inputField.updateWeight(0.3f + 0.3f * (1 - mDragOffset))
                    historyContainer.updateWeight(0.7f * mDragOffset)
                }
            }

            override fun onPanelOpened(view: View) {
                val currentWeight =
                    (inputField.layoutParams as LinearLayout.LayoutParams).weight
                val animator = if (input.text.isNullOrEmpty()) {
                    ValueAnimator.ofFloat(currentWeight, 0.0f)
                } else {
                    header.isVisible = true
                    ValueAnimator.ofFloat(currentWeight, 0.3f)
                }
                animator.addUpdateListener {
                    inputField.updateWeight(it.animatedValue as Float)
                    historyContainer.updateWeight(1.0f - it.animatedValue as Float)
                }
                animator.start()
                mainActivity.apply {
                    setSupportActionBar(historyBar)
                    if (!historyBar.isVisible) {
                        historyBar.show()
                        topAppBar.hide {
                            historyBar.isVisible = true
                            supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        }
                    }

                    addMenuProvider(menuProvider)
                }
            }

            override fun onPanelClosed(view: View) {
                inputField.animateWeight(inputField.weight(), 1.0F)
                historyContainer.animateWeight(historyContainer.weight(), 0.0F)

                header.isVisible = false
                mainActivity.apply {
                    setSupportActionBar(topAppBar)
                    historyBar.hide()
                    topAppBar.show {
                        historyBar.isVisible = false
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    }

                    removeMenuProvider(menuProvider)
                }
            }
        })
    }

    private fun Binding.addObservers() {
        historyViewModel.clickedHistory.observe(viewLifecycleOwner) {
            draggablePanel.smoothPanelClose(300)
            input.setText(it.expr)
            output.text = it.answer
        }
        calcViewModel.inputLiveData.observe(viewLifecycleOwner) {
            input.setText(it)
            calcViewModel.calculateOutput()
        }
        calcViewModel.outputLiveData.observe(viewLifecycleOwner) {
            output.text = it
        }
        calcViewModel.cursorLiveData.observe(viewLifecycleOwner) {
            input.setSelection(it)
        }

        calcViewModel.isInverseLiveData.observe(viewLifecycleOwner) {
            if (it) {
                val colorPrimary = MaterialColors.getColor(requireView(), R.attr.colorPrimary)
                val colorOnPrimary = MaterialColors.getColor(requireView(), R.attr.colorOnPrimary)
                inverseButton.backgroundTintList = ColorStateList.valueOf(colorOnPrimary)
                inverseButton.setTextColor(colorPrimary)
                sinButton.setText(R.string.asin)
                cosButton.setText(R.string.acos)
                tanButton.setText(R.string.atan)
                rootButton.setText(R.string.square)
                naturalLogButton.setText(R.string.eRaisedTo)
                log10Button.setText(R.string.tenRaisedTo)
            } else {
                inverseButton.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                val colorOnPrimary = MaterialColors.getColor(requireView(), R.attr.colorOnPrimary)
                inverseButton.setTextColor(colorOnPrimary)
                sinButton.setText(R.string.sin)
                cosButton.setText(R.string.cos)
                tanButton.setText(R.string.tan)
                rootButton.setText(R.string.root)
                naturalLogButton.setText(R.string.natural_log)
                log10Button.setText(R.string.log)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun Binding.setUpViews() {

        //Setting AutoResize For TextView
        output.setAutoSizeTextTypeUniformWithConfiguration(
            10,
            24,
            1,
            TypedValue.COMPLEX_UNIT_SP
        )

        // Don't show keyboard on focus/click
        input.showSoftInputOnFocus = false
        input.requestFocus()
    }

    private fun Binding.addListeners() {
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
            if ((it as Button).text.toString() == "ln") {
                calcViewModel.funClicked(it.text)
            } else {
                calcViewModel.otherClicked(it.text)
            }
        }
        log10Button.setOnClickListener {
            if ((it as Button).text.toString() == "log") {
                calcViewModel.funClicked(it.text)
            } else {
                calcViewModel.otherClicked(it.text)
            }
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
            calcViewModel.calculateOutput()
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

        input.apply {
            setOnClickListener {
                calcViewModel.setCursor(selectionStart)
            }
            setOnLongClickListener {
                calcViewModel.setCursor(selectionStart)
                return@setOnLongClickListener false
            }
        }

        /* Visual Stuff  */
        slideButton.setOnClickListener {
            if (slidingPaneLayout.isOpen) {
                slidingPaneLayout.closePane()
            } else {
                slidingPaneLayout.openPane()
            }


            slidingPaneLayout.addPanelSlideListener(object : SlidingPaneLayout.PanelSlideListener {
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

    }

    override fun onResume() {
        super.onResume()
        val imm: InputMethodManager? = requireContext().getSystemService()
        imm?.hideSoftInputFromWindow(binding.input.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class Binding(binding: FragmentCalculatorBinding) {
        val root = binding.root
        val historyContainer = binding.historyContainer
        val slidingPaneLayout = binding.slidingPaneLayout
        val draggablePanel = binding.draggablePanel
        val inputField = binding.inputField.root
        val input = binding.inputField.input
        val header = binding.inputField.header
        val output = binding.inputField.output
        val button0 = binding.simpleCalc.button0
        val button1 = binding.simpleCalc.button1
        val button2 = binding.simpleCalc.button2
        val button3 = binding.simpleCalc.button3
        val button4 = binding.simpleCalc.button4
        val button5 = binding.simpleCalc.button5
        val button6 = binding.simpleCalc.button6
        val button7 = binding.simpleCalc.button7
        val button8 = binding.simpleCalc.button8
        val button9 = binding.simpleCalc.button9
        val addButton = binding.simpleCalc.addButton
        val clearButton = binding.simpleCalc.clearButton
        val decimalButton = binding.simpleCalc.decimalButton
        val divideButton = binding.simpleCalc.divideButton
        val subButton = binding.simpleCalc.subButton
        val multButton = binding.simpleCalc.multButton
        val equalButton = binding.simpleCalc.equalButton
        val angleButton = binding.advCalc.angleButton
        val closeBracketButton = binding.advCalc.closeBracketButton
        val cosButton = binding.advCalc.cosButton
        val eulerNumButton = binding.advCalc.eulerNumButton
        val factorialButton = binding.advCalc.factorialButton
        val inverseButton = binding.advCalc.inverseButton
        val log10Button = binding.advCalc.log10Button
        val naturalLogButton = binding.advCalc.naturalLogButton
        val openBracketButton = binding.advCalc.openBracketButton
        val percentButton = binding.advCalc.percentButton
        val piButton = binding.advCalc.piButton
        val rootButton = binding.advCalc.rootButton
        val powerButton = binding.advCalc.powerButton
        val sinButton = binding.advCalc.sinButton
        val tanButton = binding.advCalc.tanButton
        val slideButton = binding.advCalc.slideButton
    }
}