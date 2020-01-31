package com.boxofm.crazytiles.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.boxofm.crazytiles.database.GamesDatabaseDao

class ScoreViewModelFactory(private val finalScore: Int, private val winner: Boolean, private val difficultyLevel: String, private val dataSource: GamesDatabaseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(finalScore, winner, difficultyLevel, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}