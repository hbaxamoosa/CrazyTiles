package com.boxofm.crazytiles

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.boxofm.crazytiles.databinding.ActivityMainBinding
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
}
