package com.boxofm.crazytiles.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.boxofm.crazytiles.database.GamesDatabaseDao

class InfoViewModelFactory(private val dataSource: GamesDatabaseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InfoViewModel::class.java)) {
            return InfoViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}