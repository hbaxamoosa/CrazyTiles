package com.boxofm.crazytiles.score

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.boxofm.crazytiles.database.GamesDatabaseDao

class ScoreViewModelFactory(private val finalScore: Int, private val difficultyLevel: String, private val dataSource: GamesDatabaseDao, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(finalScore, difficultyLevel, dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}