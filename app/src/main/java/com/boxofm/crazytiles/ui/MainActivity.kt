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
import androidx.preference.PreferenceManager
import com.boxofm.crazytiles.databinding.ActivityMainBinding
import com.boxofm.crazytiles.ui.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber


private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = binding.navigationView
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        PreferenceManager.setDefaultValues(this,
                R.xml.preferences, false)

        val sharedPref: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this)

        val checkboxPref: Boolean = sharedPref.getBoolean("checkbox_preference", false)
        Timber.v("%s %s", "value of checkboxPref is ", checkboxPref)

        val listPref: String? = sharedPref.getString("list_preference", "-1")
        Timber.v("%s %s", "value of listPref is ", listPref)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_songs -> {
                Toast.makeText(this, "Songs", Toast.LENGTH_LONG).show()
                Timber.i("Songs")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_albums -> {
                Toast.makeText(this, "Albums", Toast.LENGTH_LONG).show()
                Timber.i("Albums")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_artists -> {
                Toast.makeText(this, "Artists", Toast.LENGTH_LONG).show()
                Timber.i("Artists")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
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
}
