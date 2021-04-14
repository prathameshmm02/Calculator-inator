package com.inator.calculator.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.Preference
import com.calculator.inator.R

class SettingsActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            val storageRequestCode = 69
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                storageRequestCode
            )
        }
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings,  SettingsFragment())
                .commit()
        }
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {
        private lateinit var bg: Preference
        lateinit var about: Preference
        private val PICK_IMAGE_REQUEST_CODE = 100
        override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            bg = findPreference("background")!!
            bg.onPreferenceClickListener = this
            about = findPreference("about")!!
            about.onPreferenceClickListener = this
        }

        override fun onPreferenceClick(preference: Preference): Boolean {
            if (preference.key == "background") {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
                return true
            } else if (preference.key == "about") {
                startActivity(Intent(context, AboutActivity::class.java))
                return true
            }
            return false
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                val preference = PreferenceManager.getDefaultSharedPreferences(requireContext())
                val edit = preference.edit()
                edit.putString("background", data?.dataString)
                edit.apply()
            }
        }

    }
}