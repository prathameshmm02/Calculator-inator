package com.example.calculator.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.calculator.R;

public class SettingsActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            int storageRequestCode = 69;
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, storageRequestCode);
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
        Preference bg, about;
        private static final int PICK_AVATAR_REQUEST_CODE = 100;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            bg = findPreference("background");
            assert bg != null;
            bg.setOnPreferenceClickListener(this);
            about = findPreference("about");
            assert about != null;
            about.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference.getKey().equals("background")) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_AVATAR_REQUEST_CODE);
                return true;
            } else if (preference.getKey().equals("about")) {
                Log.i("S", "Anofg");
                startActivity(new Intent(getContext(), AboutActivity.class));
                return true;
            }
            return false;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_AVATAR_REQUEST_CODE) {
                SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(requireContext());
                SharedPreferences.Editor edit = preference.edit();
                edit.putString("background", data.getDataString());
                edit.apply();
            }
        }

    }
}