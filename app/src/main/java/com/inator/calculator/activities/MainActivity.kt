package com.inator.calculator.activities

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.inator.calculator.R
import com.inator.calculator.adapters.ViewPagerAdapter
import com.inator.calculator.extras.ZoomOutPageTransformer
import com.inator.calculator.fragments.CalculatorFragment
import com.inator.calculator.fragments.ConverterFragment
import com.inator.calculator.fragments.CurrencyFragment
import com.inator.calculator.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_calculator.*


class MainActivity : AppCompatActivity() {

    private var _menu: Menu? = null
    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Calculator)
        setContentView(R.layout.activity_main)
        setSupportActionBar(topAppBar)
        setupHistoryPanel()
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
                        0 -> tab.setIcon(R.drawable.ic_filled_calculator)

                        1 -> tab.setIcon(R.drawable.ic_filled_converter)

                        2 -> tab.setIcon(R.drawable.ic_filled_currency)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        0 -> tab.setIcon(R.drawable.ic_outline_calculator)

                        1 -> tab.setIcon(R.drawable.ic_outline_converter)

                        2 -> tab.setIcon(R.drawable.ic_outline_currency)
                    }
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
                setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
                    dismiss()
                }
                show()
            }
        }
    }

    private fun showDelete() {
        _menu?.findItem(R.id.deleteHistory)?.isVisible = true
    }

    private fun hideDelete() {
        _menu?.findItem(R.id.deleteHistory)?.isVisible = false
    }

    private fun setupHistoryPanel() {
        historyViewModel.isHistoryOpen.observe(this, {
            if (it) {
                setSupportActionBar(historyBar)
                if (!historyBar.isVisible) {
                    ObjectAnimator.ofFloat(historyBar, "alpha", 0f, 1f).apply {
                        duration = 250
                        start()
                    }
                    ObjectAnimator.ofFloat(topAppBar, "alpha", 1f, 0f).apply {
                        duration = 250
                        doOnEnd {
                            historyBar.isVisible = true
                            supportActionBar?.setDisplayHomeAsUpEnabled(true)
                            showDelete()
                        }
                        start()
                    }
                }
            } else {
                setSupportActionBar(topAppBar)
                ObjectAnimator.ofFloat(historyBar, "alpha", 1f, 0f).apply {
                    duration = 250
                    start()
                }
                ObjectAnimator.ofFloat(topAppBar, "alpha", 0f, 1f).apply {
                    duration = 250
                    doOnEnd {
                        historyBar.isVisible = false
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        hideDelete()
                    }
                    start()
                }
            }
        })
    }

    override fun onBackPressed() {
        if (historyViewModel.isHistoryOpen.value == true) {
            draggablePanel.smoothPanelClose(300)
        } else {
            super.onBackPressed()
        }
    }
}