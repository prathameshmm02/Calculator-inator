package com.inator.calculator.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.calculator.inator.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.inator.calculator.adpters.ViewPagerAdapter
import com.inator.calculator.fragments.CalculatorFragment
import com.inator.calculator.fragments.ConverterFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Calculator)
        setContentView(R.layout.activity_main)
        setSupportActionBar(topAppBar)
        val pagerAdapter = ViewPagerAdapter(supportFragmentManager, 1)
        pagerAdapter.addFragment(CalculatorFragment())
        pagerAdapter.addFragment(ConverterFragment())
        pager.adapter = pagerAdapter

        tabLayout.apply {
            setupWithViewPager(pager)
            getTabAt(0)?.setIcon(R.drawable.ic_filled_calculate)
            getTabAt(1)?.setIcon(R.drawable.ic_outline_converter)
            addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    if (tab.position == 0) {
                        tab.setIcon(R.drawable.ic_filled_calculate)
                    } else if (tab.position == 1) {
                        tab.setIcon(R.drawable.ic_filled_converter)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    if (tab.position == 0) {
                        tab.setIcon(R.drawable.ic_outline_calculator)
                    } else if (tab.position == 1) {
                        tab.setIcon(R.drawable.ic_outline_converter)
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