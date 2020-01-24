package com.boxofm.crazytiles.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.boxofm.crazytiles.R
import timber.log.Timber

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            val sharedPref: SharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this.context)

            when (sharedPref.getString("list_preference", "unknown")) {
                ("Easy") -> {
                    val listPreference = findPreference<ListPreference>("list_preference")!!
                    listPreference.summary = "Easy"
                }
                ("Medium") -> {
                    val listPreference = findPreference<ListPreference>("list_preference")!!
                    listPreference.summary = "Medium"
                }
                ("Hard") -> {
                    val listPreference = findPreference<ListPreference>("list_preference")!!
                    listPreference.summary = "Hard"
                }
                else -> Timber.v("%s %s", "game level is ", "unknown")
            }
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            val stringValue: String = key.toString()
            val selection: String = sharedPreferences?.getString(key, "unknown")!!
            Timber.v("%s %s", "value of selection is ", selection)

            if (stringValue == "list_preference") {
                val listPreference = findPreference<ListPreference>(key.toString())!!
                listPreference.summary = selection
            }
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }
    }
}