package com.boxofm.crazytiles

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.boxofm.crazytiles.databinding.ActivityMainBinding
import com.boxofm.crazytiles.ui.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)

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
