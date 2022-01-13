package com.boxofm.crazytiles.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boxofm.crazytiles.database.Games
import com.boxofm.crazytiles.database.GamesDatabaseDao
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * ViewModel for the final screen showing the score
 */
class ScoreViewModel(finalScore: Int,
                     winner: Boolean,
                     difficultyLevel: String,
                     val database: GamesDatabaseDao) : ViewModel() {

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

    private val _gamesHistory = MutableLiveData<List<Games>>()
    val gamesHistory: LiveData<List<Games>>
        get() = _gamesHistory

    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _winner = MutableLiveData<Boolean>()
    val winner: LiveData<Boolean>
        get() = _winner

    init {
        _score.value = finalScore
        /*Timber.v("%s %s", "value of _score.value is ", _score.value)*/
        _winner.value = winner
        /*Timber.v("%s %s", "value of _winner.value is ", _winner.value)*/

        val tempList: MutableList<Games> = ArrayList()
        tempList.add(Games(finalScore))
        _gamesHistory.value = tempList

        saveGame(winner, difficultyLevel)
    }

    /**
     * Sets the game score and updates the database.
     */
    private fun saveGame(winner: Boolean, level: String) {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                val thisScore: Int = if (winner) 1 else 0
                Timber.v("%s %s", "value of winner is ", winner)
                Timber.v("%s %s", "value of thisScore is ", thisScore)
                val game = Games(wins = thisScore, level = level)
                Timber.v(
                    "%s %s %s %s",
                    "value of saveGame is",
                    game.gameCount,
                    game.wins,
                    game.level
                )
                database.insert(game)
            }
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