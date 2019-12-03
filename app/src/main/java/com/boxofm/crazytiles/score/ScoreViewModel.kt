package com.boxofm.crazytiles.score

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boxofm.crazytiles.database.Games
import com.boxofm.crazytiles.database.GamesDatabaseDao
import kotlinx.coroutines.*

/**
 * ViewModel for the final screen showing the score
 */
class ScoreViewModel(finalScore: Int,
                     val database: GamesDatabaseDao,
                     application: Application) : ViewModel() {

    /** Coroutine setup variables */

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this scope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val scoresHistory = MutableLiveData<Games?>()

    private val gamesHistory = database.getAllGames()

    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    init {
        _score.value = finalScore
        saveGame(finalScore)
        getGamesMostRecent()
    }

    /**
     * Sets the game score and updates the database.
     */
    private fun saveGame(finalScore: Int) {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                val game = Games(totalGames = 1, wins = finalScore)
                database.update(game)
            }
        }
    }

    private fun getGamesMostRecent() {
        uiScope.launch {
            scoresHistory.value = getGamesHistory()
        }
    }

    /**
     *  Handling the case of the stopped app or forgotten recording,
     *  the start and end times will be the same.j
     *
     *  If the start time and end time are not the same, then we do not have an unfinished
     *  recording.
     */
    private suspend fun getGamesHistory(): Games? {
        return withContext(Dispatchers.IO) {
            var game = database.getLatestGame()
            game
        }
    }

    fun onPlayAgain() {
        _eventPlayAgain.value = true
    }

    fun onPlayAgainComplete() {
        _eventPlayAgain.value = false
    }

    /**
     * Cancels all coroutines when the ViewModel is cleared, to cleanup any pending work.
     *
     * onCleared() gets called when the ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}