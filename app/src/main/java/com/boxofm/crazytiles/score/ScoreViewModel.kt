package com.boxofm.crazytiles.score

import android.app.Application
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
                     difficultyLevel: String,
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

    private val _gamesHistory = MutableLiveData<List<Games>>()
    val gamesHistory: LiveData<List<Games>>
        get() = _gamesHistory

    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    init {
        _score.value = finalScore

        val tempList: MutableList<Games> = ArrayList<Games>()
        tempList.add(Games(finalScore))
        _gamesHistory.value = tempList

        saveGame(finalScore, difficultyLevel)
        getGamesMostRecent()
        getAllGamesPlayed()
    }

    /**
     * Sets the game score and updates the database.
     */
    private fun saveGame(finalScore: Int, level: String) {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                // val game = Games(totalGames = 1, wins = finalScore)
                val game = Games(wins = finalScore, level = level)
                Timber.v("%s %s %s %s", "saveGame is", game.gameCount, game.wins, game.level)
                database.insert(game)
            }
        }
    }

    private fun getGamesMostRecent() {
        uiScope.launch {
            scoresHistory.value = getGamesHistory()
            Timber.v("coroutine for getGamesMostRecent()")
            _score.value = scoresHistory.value?.wins
        }
    }

    private suspend fun getGamesHistory(): Games? {
        return withContext(Dispatchers.IO) {
            val game = database.getLatestGame()
            Timber.v("%s %s %s %s", "getGamesHistory is", game?.gameCount, game?.wins, game?.level)
            game
        }
    }

    private fun getAllGamesPlayed() {
        uiScope.launch {
            _gamesHistory.value = getAllGames()
            Timber.v("coroutine for getAllGames()")
        }
    }

    private suspend fun getAllGames(): List<Games> {
        return withContext(Dispatchers.IO) {
            val games = database.getAllGames()
            Timber.v("%s %s %s %s", "getAllGames is", games.get(0).gameCount, games.get(0).wins, games.get(0).level)
            Timber.v("%s %s", "total number of games", games.count())
            games
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