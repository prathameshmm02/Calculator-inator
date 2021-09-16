package com.inator.calculator.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
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
        historyViewModel.isHistoryOpen.observe(this, {
            if (it) {
                if (!historyBar.isVisible) {
                    setSupportActionBar(historyBar)
                    ObjectAnimator.ofFloat(historyBar, "alpha", 0f, 1f).apply {
                        duration = 250
                        start()
                    }
                    ObjectAnimator.ofFloat(topAppBar, "alpha", 1f, 0f).apply {
                        duration = 250
                        addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                historyBar.visibility = View.VISIBLE
                                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                                showDelete()
                            }
                        })
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
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            historyBar.visibility = View.GONE
                            supportActionBar?.setDisplayHomeAsUpEnabled(false)
                            hideDelete()
                        }
                    })
                    start()
                }

            }
        })
        setUpViews()
    }


    private fun setUpViews() {
        val pagerAdapter = ViewPagerAdapter(supportFragmentManager, 1)
        pagerAdapter.apply {
            addFragment(CalculatorFragment())
            addFragment(ConverterFragment())
            addFragment(CurrencyFragment())
        }
        pager.adapter = pagerAdapter
        pager.offscreenPageLimit = 2
        pager.setPageTransformer(true, ZoomOutPageTransformer())

        tabLayout.apply {
            setupWithViewPager(pager)

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
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage(resources.getString(R.string.delete_message))
        alertDialog.setTitle(resources.getString(R.string.delete_title))
        alertDialog.setIcon(R.drawable.ic_delete_history)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok") { _, _ ->
            historyViewModel.deleteAllHistory()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun showDelete() {
        _menu?.findItem(R.id.deleteHistory)?.isVisible = true
    }

    private fun hideDelete() {
        _menu!!.findItem(R.id.deleteHistory).isVisible = false
    }

    override fun onBackPressed() {
        if (historyViewModel.isHistoryOpen.value == true) {
            draggablePanel.smoothPanelClose(300)
        } else {
            this.finish()
        }
    }

}