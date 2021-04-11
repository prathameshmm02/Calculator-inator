package com.example.calculator.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calculator.NonSwipeableViewPager;
import com.example.calculator.R;
import com.example.calculator.adpters.ViewPagerAdapter;
import com.example.calculator.fragments.CalculatorFragment;
import com.example.calculator.fragments.ConverterFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    NonSwipeableViewPager viewPager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.topAppBar));
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 1);
        pagerAdapter.addFragment(new CalculatorFragment());
        pagerAdapter.addFragment(new ConverterFragment());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(this.viewPager);
        Objects.requireNonNull(this.tabLayout.getTabAt(0)).setIcon(R.drawable.ic_filled_calculate);
        Objects.requireNonNull(this.tabLayout.getTabAt(1)).setIcon(R.drawable.ic_outline_converter);
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tab.setIcon(R.drawable.ic_filled_calculate);
                } else if (tab.getPosition() == 1) {
                    tab.setIcon(R.drawable.ic_filled_converter);
                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tab.setIcon(R.drawable.ic_outline_calculator);
                } else if (tab.getPosition() == 1) {
                    tab.setIcon(R.drawable.ic_outline_converter);
                }
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settingButton) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}


