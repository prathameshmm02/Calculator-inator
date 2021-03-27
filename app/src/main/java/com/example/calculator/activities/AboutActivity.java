package com.example.calculator.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.calculator.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setTitle("About");
        ImageView imageView = findViewById(R.id.nameImage);
        Glide.with(this)
                .load(R.drawable.name)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }
}