package com.example.calculator.activities;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.calculator.R;
import com.example.calculator.adpters.ViewPagerAdapter;
import com.example.calculator.extras.ZoomOutPageTransformer;
import com.example.calculator.fragments.CalculatorFragment;
import com.example.calculator.fragments.ConverterFragment;

public class MainActivity extends AppCompatActivity {
    ImageButton calc, conv;
    View indicator, pseudoIndicator;
    ViewPager2 viewPager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.topAppBar));
        calc = findViewById(R.id.calculator);
        conv = findViewById(R.id.converter);
        indicator = findViewById(R.id.indicator);
        pseudoIndicator = findViewById(R.id.indicator2);
        viewPager = findViewById(R.id.pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this);
        pagerAdapter.addFragment(new CalculatorFragment());
        pagerAdapter.addFragment(new ConverterFragment());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());

        calc.setOnClickListener(v -> {
            viewPager.setCurrentItem(0, true);
            calc.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_filled_calculate));
            conv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_outline_converter));
            ObjectAnimator animation = ObjectAnimator.ofFloat(indicator, "translationX", 0);
            animation.setDuration(100);
            animation.start();

        });
        conv.setOnClickListener(v -> {
            int defaultPosition;
            int[] position = new int[2];
            pseudoIndicator.getLocationInWindow(position);
            int nextLocation = position[0];
            indicator.getLocationInWindow(position);
            if (viewPager.getCurrentItem() == 0) {
                defaultPosition = position[0];
                calc.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_outline_calculator));
                conv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_filled_converter));
                ObjectAnimator animation = ObjectAnimator.ofFloat(indicator, "translationX", nextLocation - defaultPosition);
                animation.setDuration(100);
                Log.i("Next", String.valueOf(indicator.getWidth()));
                animation.start();
            }
            viewPager.setCurrentItem(1, true);

        });
        viewPager.setUserInputEnabled(false);
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