package com.inator.calculator.activities

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.inator.calculator.R
import com.inator.calculator.adapters.ViewPagerAdapter
import com.inator.calculator.extensions.*
import com.inator.calculator.extras.ZoomOutPageTransformer
import com.inator.calculator.fragments.CalculatorFragment
import com.inator.calculator.fragments.ConverterFragment
import com.inator.calculator.fragments.CurrencyFragment
import com.inator.calculator.viewmodel.HistoryViewModel
import com.inator.calculator.views.DraggablePanel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_calculator.*
import kotlinx.android.synthetic.main.layout_input_field.*


class MainActivity : AppCompatActivity() {

    private var _menu: Menu? = null
    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Calculator)
        setContentView(R.layout.activity_main)
        setSupportActionBar(topAppBar)
        window.decorView.post {
            setupHistoryPanel()
        }
        setUpViews()
    }

    private fun setUpViews() {
        val pagerAdapter = ViewPagerAdapter(this)
        pagerAdapter.apply {
            addFragment(CalculatorFragment())
            addFragment(ConverterFragment())
            addFragment(CurrencyFragment())
        }
        pager.apply {
            adapter = pagerAdapter
            offscreenPageLimit = 2
            setPageTransformer(ZoomOutPageTransformer())
            isUserInputEnabled = false
        }

        tabLayout.apply {
            TabLayoutMediator(
                tabLayout, pager
            ) { _: TabLayout.Tab, _: Int -> }.attach()

            getTabAt(0)?.setIcon(R.drawable.ic_filled_calculator)
            getTabAt(1)?.setIcon(R.drawable.ic_outline_converter)
            getTabAt(2)?.setIcon(R.drawable.ic_outline_currency)

            addOnTabSelectedListener(object : OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        0 -> {
                            tab.setIcon(R.drawable.ic_filled_calculator)
                            // Hide keyboard
                            getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(
                                this@apply.windowToken,
                                0
                            )
                        }

                        1 -> tab.setIcon(R.drawable.ic_filled_converter)

                        2 -> tab.setIcon(R.drawable.ic_filled_currency)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    tab.setIcon(
                        when (tab.position) {
                            0 -> R.drawable.ic_outline_calculator

                            1 -> R.drawable.ic_outline_converter

                            2 -> R.drawable.ic_outline_currency

                            else -> R.drawable.ic_outline_calculator
                        }
                    )
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        _menu = menu
        menuInflater.inflate(R.menu.app_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingButton -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.deleteHistory -> {
                createDialog()
            }
            android.R.id.home -> {
                draggablePanel.smoothPanelClose(300)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createDialog() {
        AlertDialog.Builder(this).create().apply {
            setMessage(resources.getString(R.string.delete_message))
            setTitle(resources.getString(R.string.delete_title))
            setIcon(R.drawable.ic_delete_history)
            setButton(AlertDialog.BUTTON_POSITIVE, "Ok") { _, _ ->
                historyViewModel.deleteAllHistory()
            }
            setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
                dismiss()
            }
            show()
        }
    }

    private fun showDelete() {
        _menu?.findItem(R.id.deleteHistory)?.isVisible = true
    }

    private fun hideDelete() {
        _menu?.findItem(R.id.deleteHistory)?.isVisible = false
    }

    private fun setupHistoryPanel() {
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
                val currentWeight = (inputField.layoutParams as LinearLayout.LayoutParams).weight
                val animator = if (input.text.isNullOrEmpty()) {
                    ValueAnimator.ofFloat(currentWeight, 0.0f)
                } else {
                    header.visibility = View.VISIBLE
                    ValueAnimator.ofFloat(currentWeight, 0.3f)
                }
                animator.addUpdateListener {
                    inputField.updateWeight(it.animatedValue as Float)
                    historyContainer.updateWeight(1.0f - it.animatedValue as Float)
                }
                animator.start()
                setSupportActionBar(historyBar)
                if (!historyBar.isVisible) {
                    historyBar.show()
                    topAppBar.hide {
                        historyBar.isVisible = true
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        showDelete()
                    }
                }
            }

            override fun onPanelClosed(view: View) {
                inputField.animateWeight(inputField.weight(), 1.0F)
                historyContainer.animateWeight(historyContainer.weight(), 0.0F)

                header.isVisible = false
                setSupportActionBar(topAppBar)
                historyBar.hide()
                topAppBar.show {
                    historyBar.isVisible = false
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    hideDelete()
                }
            }
        })
    }

    override fun onBackPressed() {
        if (draggablePanel.isOpen()) {
            draggablePanel.smoothPanelClose(300)
        } else {
            super.onBackPressed()
        }
    }
}