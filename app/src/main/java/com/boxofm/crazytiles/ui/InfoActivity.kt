package com.boxofm.crazytiles.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.boxofm.crazytiles.R
import com.boxofm.crazytiles.databinding.ActivityInfoBinding
import timber.log.Timber

private lateinit var binding: ActivityInfoBinding

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info)

        Timber.v("inside Info")
    }
}