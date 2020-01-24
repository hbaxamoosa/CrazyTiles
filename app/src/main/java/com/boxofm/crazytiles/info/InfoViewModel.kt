package com.boxofm.crazytiles.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boxofm.crazytiles.database.GamesDatabaseDao
import kotlinx.coroutines.*
import timber.log.Timber
import java.text.NumberFormat
import java.util.*

class InfoViewModel(val database: GamesDatabaseDao) : ViewModel() {

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

    private val _totalGames = MutableLiveData<Int>()
    val totalGames: LiveData<Int>
        get() = _totalGames

    private val _winsEasy = MutableLiveData<Int>()
    val winsEasy: LiveData<Int>
        get() = _winsEasy

    private val _winsMedium = MutableLiveData<Int>()
    val winsMedium: LiveData<Int>
        get() = _winsMedium

    private val _winsHard = MutableLiveData<Int>()
    val winsHard: LiveData<Int>
        get() = _winsHard

    private val _lossEasy = MutableLiveData<Int>()
    val lossEasy: LiveData<Int>
        get() = _lossEasy

    private val _lossMedium = MutableLiveData<Int>()
    val lossMedium: LiveData<Int>
        get() = _lossMedium

    private val _lossHard = MutableLiveData<Int>()
    val lossHard: LiveData<Int>
        get() = _lossHard

    private val _percentageEasy = MutableLiveData<String>()
    val percentageEasy: LiveData<String>
        get() = _percentageEasy

    private val _percentageMedium = MutableLiveData<String>()
    val percentageMedium: LiveData<String>
        get() = _percentageMedium

    private val _percentageHard = MutableLiveData<String>()
    val percentageHard: LiveData<String>
        get() = _percentageHard

    private val _gameReset = MutableLiveData<Boolean>()
    val gameReset: LiveData<Boolean>
        get() = _gameReset

    private val levelEasy = "Easy"
    private val levelMedium = "Medium"
    private val levelHard = "Hard"

    init {
        populateScoreHistory()
    }

    private fun populateScoreHistory() {
        getTotalGames()
        getWinsByLevels(levelEasy)
        getWinsByLevels(levelMedium)
        getWinsByLevels(levelHard)
        getLossByLevels(levelEasy)
        getLossByLevels(levelMedium)
        getLossByLevels(levelHard)
        getPercantageByLevels(levelEasy)
        getPercantageByLevels(levelMedium)
        getPercantageByLevels(levelHard)
    }

    private fun getTotalGames() {
        uiScope.launch {
            _totalGames.value = getCountOfAllGames()
//            Timber.v("%s %s", "value of _totalGames.value is ", _totalGames.value)
        }
    }

    private suspend fun getCountOfAllGames(): Int {
        return withContext(Dispatchers.IO) {
            val count: Int = database.getTotalNumberOfGames()!!
//            Timber.v("%s %s", "value of count is ", count)
            count
        }
    }

    private fun getWinsByLevels(level: String) {
        uiScope.launch {
            when (level) {
                "Easy" -> {
                    _winsEasy.value = getCountByLevel(level)
//                    Timber.v("%s %s", "value of _winsEasy.value is ", _winsEasy.value)
                }
                "Medium" -> {
                    _winsMedium.value = getCountByLevel(level)
//                    Timber.v("%s %s", "value of _winsMedium.value is ", _winsMedium.value)

                }
                "Hard" -> {
                    _winsHard.value = getCountByLevel(level)
//                    Timber.v("%s %s", "value of _winsHard.value is ", _winsHard.value)

                }
            }
        }
    }

    private suspend fun getCountByLevel(level: String): Int {
        return withContext(Dispatchers.IO) {
            val count: Int = database.getCountByLevel(level)!!
//            Timber.v("%s %s %s %s", "value of ", level, " is ", count)
            count
        }
    }

    private fun getLossByLevels(level: String) {
        uiScope.launch {
            when (level) {
                "Easy" -> {
                    _lossEasy.value = getLossByLevel(levelEasy)
//                    Timber.v("%s %s", "value of _lossEasy.value is ", _lossEasy.value)
                }
                "Medium" -> {
                    _lossMedium.value = getLossByLevel(levelMedium)
//                    Timber.v("%s %s", "value of _lossMedium.value is ", _lossMedium.value)
                }
                "Hard" -> {
                    _lossHard.value = getLossByLevel(levelHard)
//                    Timber.v("%s %s", "value of _lossHard.value is ", _lossHard.value)
                }
            }
        }
    }

    private suspend fun getLossByLevel(level: String): Int {
        return withContext(Dispatchers.IO) {
            val total: Int = database.getTotalNumberOfGames()!!
            val winCount: Int = database.getCountByLevel(level)!!
//            Timber.v("%s %s %s %s", "value of ", level, " is ", total)
//            Timber.v("%s %s %s %s", "value of ", level, " is ", winCount)
            total - winCount
        }
    }

    private fun getPercantageByLevels(level: String) {
        uiScope.launch {
            when (level) {
                "Easy" -> {
                    _percentageEasy.value = getPercentageByLevel(levelEasy)
//                    Timber.v("%s %s", "value of _lossEasy.value is ", _lossEasy.value)
                }
                "Medium" -> {
                    _percentageMedium.value = getPercentageByLevel(levelMedium)
//                    Timber.v("%s %s", "value of _lossMedium.value is ", _lossMedium.value)
                }
                "Hard" -> {
                    _percentageHard.value = getPercentageByLevel(levelHard)
//                    Timber.v("%s %s", "value of _lossHard.value is ", _lossHard.value)
                }
            }
        }
    }

    private suspend fun getPercentageByLevel(level: String): String {
        return withContext(Dispatchers.IO) {
            val total: Double = database.getTotalNumberOfGamesDouble()!!
            val winCount: Double = database.getCountByLevelDouble(level)!!
            Timber.v("%s %s %s %s", "value of ", level, " is total ", total)
            Timber.v("%s %s %s %s", "value of ", level, " is winCount ", winCount)
            val format: NumberFormat = NumberFormat.getPercentInstance(Locale.US)
            val percentage: String
            if (total == 0.0) {
                percentage = "0%"
            } else {
                percentage = format.format(winCount / total)
                Timber.v("%s %s %s %s", "value of ", level, " is percentage ", percentage)
//            (winCount / total) * 100
            }
            percentage
        }
    }

    fun resetStats() {
        uiScope.launch {
            clearGamesDB()
        }
        _gameReset.value = true
    }

    private suspend fun clearGamesDB() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    fun resetStatsComplete() {
        _gameReset.value = false
        populateScoreHistory()
        Timber.v("%s %s", "value of _gameReset.value is ", _gameReset.value)
        Timber.v("%s %s", "value of populateScoreHistory", "")
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