package com.inator.calculator.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.inator.calculator.R
import com.inator.calculator.repository.Data

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

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

