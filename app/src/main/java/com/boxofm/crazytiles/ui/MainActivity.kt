package com.boxofm.crazytiles

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.boxofm.crazytiles.databinding.ActivityMainBinding
import com.boxofm.crazytiles.ui.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import timber.log.Timber


private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.myNavHostFragment) as NavHostFragment? ?: return

        // Set up Action Bar
        val navController = host.navController

        setupBottomNavMenu(navController)

        PreferenceManager.setDefaultValues(this,
                R.xml.preferences, false)

        val sharedPrefs: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this)

        // Firebase Analytics
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, sharedPrefs.getString("list_preference", "unknown")!!)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        // Firebase Remote Config
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        fetchRemoteConfigValues()
    }

    fun fetchRemoteConfigValues() {

        var easy: Int
        var medium: Int
        var hard: Int

        Timber.v("%s %s", "value of difficulty_level_easy is ", remoteConfig.getString("difficulty_level_easy"))
        Timber.v("%s %s", "value of difficulty_level_medium is ", remoteConfig.getString("difficulty_level_medium"))
        Timber.v("%s %s", "value of difficulty_level_hard is ", remoteConfig.getString("difficulty_level_hard"))

        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Timber.v("Config params updated: $updated")
                        Toast.makeText(this, "Fetch and activate succeeded",
                                Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Fetch failed",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }
}
