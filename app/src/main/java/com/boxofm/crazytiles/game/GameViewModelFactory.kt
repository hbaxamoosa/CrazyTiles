package com.boxofm.crazytiles.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameViewModelFactory(private val difficultyLevel: String, private val time: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(difficultyLevel, time) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}