package com.inator.calculator.activities

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.inator.calculator.R

class SettingsActivity : AppCompatActivity() {



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener//, PreferenceChangeListener {
    {

        private val PICK_IMAGE_REQUEST_CODE = 100
        private lateinit var defaultPreferences: SharedPreferences
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            defaultPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val bg: Preference? = findPreference("background")
            val about: Preference? = findPreference("about")

            if (bg != null) {
                bg.onPreferenceClickListener = this
            }
            if (about != null) {
                about.onPreferenceClickListener = this
            }
        }

        override fun onPreferenceClick(preference: Preference): Boolean {
            if (preference.key == "background") {
                if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val storageRequestCode = 69
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        storageRequestCode
                    )
                }
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

                val edit = defaultPreferences.edit()
                edit.putString("background", data?.dataString)
                edit.apply()
            }
        }


    }
}