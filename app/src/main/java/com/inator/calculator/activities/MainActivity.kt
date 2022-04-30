package com.inator.calculator.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.inator.calculator.R
import com.inator.calculator.adapters.ViewPagerAdapter
import com.inator.calculator.databinding.ActivityMainBinding
import com.inator.calculator.extras.ZoomOutPageTransformer
import com.inator.calculator.fragments.CalculatorFragment
import com.inator.calculator.fragments.ConverterFragment
import com.inator.calculator.fragments.CurrencyFragment
import com.inator.calculator.viewmodel.HistoryViewModel


class MainActivity : AppCompatActivity() {

    private val historyViewModel: HistoryViewModel by viewModels()
    private lateinit var binding : ActivityMainBinding

    val topAppBar get() = binding.topAppBar
    val historyBar get() = binding.historyBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Calculator)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        setUpViews()
    }

    private fun setUpViews() {
        val pagerAdapter = ViewPagerAdapter(this)
        pagerAdapter.apply {
            addFragment(CalculatorFragment())
            addFragment(ConverterFragment())
            addFragment(CurrencyFragment())
        }
        binding.pager.apply {
            adapter = pagerAdapter
            offscreenPageLimit = 2
            setPageTransformer(ZoomOutPageTransformer())
            isUserInputEnabled = false
        }

        binding.tabLayout.apply {
            TabLayoutMediator(
                this, binding.pager
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createDialog() {
        MaterialAlertDialogBuilder(this).create().apply {
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
}