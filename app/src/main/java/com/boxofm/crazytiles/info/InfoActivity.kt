package com.boxofm.crazytiles.info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.boxofm.crazytiles.R
import com.boxofm.crazytiles.database.GamesDatabase
import com.boxofm.crazytiles.databinding.ActivityInfoBinding

private lateinit var binding: ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var viewModel: InfoViewModel
    private lateinit var viewModelFactory: InfoViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info)

        val dataSource = GamesDatabase.getInstance(this).gamesDatabaseDao
        viewModelFactory = InfoViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(InfoViewModel::class.java)

        // Bind the activity to the ViewModel
        binding.infoViewModel = viewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this
    }
}