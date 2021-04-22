package com.inator.calculator.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

class MainActivity : AppCompatActivity() {

    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Calculator)
        setContentView(R.layout.activity_main)
        setSupportActionBar(topAppBar)
//        historyViewModel.isHistoryOpen.observe(this, {
//            if (it) {
//                setSupportActionBar(topAppBar)
//            } else {
//                setSupportActionBar(toolbar)
//            }
//
//        })
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
        menuInflater.inflate(R.menu.app_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settingButton) {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}