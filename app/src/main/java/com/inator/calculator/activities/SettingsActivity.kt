package com.inator.calculator.activities

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.inator.calculator.R
import com.inator.calculator.databinding.ActivityMainBinding
import com.inator.calculator.databinding.ActivitySettingsBinding
import com.inator.calculator.repository.Data

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private lateinit var defaultPreferences: SharedPreferences
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            defaultPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

            setPreferencesFromResource(R.xml.root_preferences, rootKey)


            val appTheme: ListPreference? = findPreference("app_theme")

            appTheme?.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, newValue ->
                    Data.getInstance(requireContext()).setTheme(newValue.toString())
                    true
                }
        }
    }
}

