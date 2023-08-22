package com.boxofm.crazytiles.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.boxofm.crazytiles.R
import timber.log.Timber

/**
 * ViewModel containing all the logic needed to run the game
 */
class GameViewModel(difficultyLevel: String, time: Long) : ViewModel() {

    enum class GameDifficultyLevel {
        EASY,
        MEDIUM,
        HARD
    }

    companion object {
        // This is when the game is over
        private const val DONE = 0L

        // This is the number of milliseconds in a second
        private const val ONE_SECOND = 1000L
    }

    // Set TextView background color for each tile
    private val _tileOneColor = MutableLiveData<Int>()
    val tileOneColor: LiveData<Int>
        get() = _tileOneColor

    private val _tileTwoColor = MutableLiveData<Int>()
    val tileTwoColor: LiveData<Int>
        get() = _tileTwoColor

    private val _tileThreeColor = MutableLiveData<Int>()
    val tileThreeColor: LiveData<Int>
        get() = _tileThreeColor

    private val _tileFourColor = MutableLiveData<Int>()
    val tileFourColor: LiveData<Int>
        get() = _tileFourColor

    private val _tileFiveColor = MutableLiveData<Int>()
    val tileFiveColor: LiveData<Int>
        get() = _tileFiveColor

    private val _tileSixColor = MutableLiveData<Int>()
    val tileSixColor: LiveData<Int>
        get() = _tileSixColor

    private val _tileSevenColor = MutableLiveData<Int>()
    val tileSevenColor: LiveData<Int>
        get() = _tileSevenColor

    private val _tileEightColor = MutableLiveData<Int>()
    val tileEightColor: LiveData<Int>
        get() = _tileEightColor

    private val _tileNineColor = MutableLiveData<Int>()
    val tileNineColor: LiveData<Int>
        get() = _tileNineColor

    private val _tileTenColor = MutableLiveData<Int>()
    val tileTenColor: LiveData<Int>
        get() = _tileTenColor

    private val _tileElevenColor = MutableLiveData<Int>()
    val tileElevenColor: LiveData<Int>
        get() = _tileElevenColor

    private val _tileTwelveColor = MutableLiveData<Int>()
    val tileTwelveColor: LiveData<Int>
        get() = _tileTwelveColor

    private val _tileThirteenColor = MutableLiveData<Int>()
    val tileThirteenColor: LiveData<Int>
        get() = _tileThirteenColor

    private val _tileFourteenColor = MutableLiveData<Int>()
    val tileFourteenColor: LiveData<Int>
        get() = _tileFourteenColor

    private val _tileFifteenColor = MutableLiveData<Int>()
    val tileFifteenColor: LiveData<Int>
        get() = _tileFifteenColor

    private val _tileSixteenColor = MutableLiveData<Int>()
    val tileSixteenColor: LiveData<Int>
        get() = _tileSixteenColor

    private val timer: CountDownTimer

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    // The String version of the current time
    val currentTimeString = currentTime.switchMap { time ->
        val formattedTime = DateUtils.formatElapsedTime(time)
        MutableLiveData(formattedTime)
    }

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // Current state of the game: Win = True, Loss = False
    private val _winner = MutableLiveData<Boolean>()
    val winner: LiveData<Boolean>
        get() = _winner

    // Event which triggers the end of the game
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private val _gameStarted = MutableLiveData<Boolean>()
    val gameStarted: LiveData<Boolean>
        get() = _gameStarted


    private val _gameDifficultyLevel = MutableLiveData<GameDifficultyLevel>()
    private val gameDifficultyLevel: LiveData<GameDifficultyLevel>
        get() = _gameDifficultyLevel

    init {
        _score.value = 0
        _winner.value = false
        _eventGameFinish.value = false
        _gameStarted.value = false

        // set tile colors
        when (difficultyLevel) {
            "Easy" -> {
                _gameDifficultyLevel.value = GameDifficultyLevel.EASY
                setup2x2()
            }
            "Medium" -> {
                _gameDifficultyLevel.value = GameDifficultyLevel.MEDIUM
                setup3x3()
            }
            "Hard" -> {
                _gameDifficultyLevel.value = GameDifficultyLevel.HARD
                setup4x4()
            }
            else -> Timber.v("value of BAD")
        }

        // Creates a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(time /*COUNTDOWN_TIME*/, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventGameFinish.value = true
            }
        }
    }

    fun startGame() {
        _gameStarted.value = true
        timer.start()
    }

    private fun setup2x2() {
        _tileOneColor.value = R.color.yellow
        _tileTwoColor.value = R.color.green
        _tileThreeColor.value = R.color.blue
        _tileFourColor.value = R.color.red
    }

    private fun setup3x3() {
        _tileOneColor.value = R.color.blue
        _tileTwoColor.value = R.color.yellow
        _tileThreeColor.value = R.color.green
        _tileFourColor.value = R.color.red
        _tileFiveColor.value = R.color.blue
        _tileSixColor.value = R.color.yellow
        _tileSevenColor.value = R.color.green
        _tileEightColor.value = R.color.yellow
        _tileNineColor.value = R.color.red
    }

    private fun setup4x4() {
        _tileOneColor.value = R.color.red
        _tileTwoColor.value = R.color.green
        _tileThreeColor.value = R.color.blue
        _tileFourColor.value = R.color.yellow
        _tileFiveColor.value = R.color.blue
        _tileSixColor.value = R.color.yellow
        _tileSevenColor.value = R.color.green
        _tileEightColor.value = R.color.red
        _tileNineColor.value = R.color.green
        _tileTenColor.value = R.color.red
        _tileElevenColor.value = R.color.yellow
        _tileTwelveColor.value = R.color.blue
        _tileThirteenColor.value = R.color.yellow
        _tileFourteenColor.value = R.color.green
        _tileFifteenColor.value = R.color.blue
        _tileSixteenColor.value = R.color.red
    }

    /** Methods for buttons presses **/
    fun tileOneClicked() {
        if (_gameStarted.value!!) {
            _tileOneColor.value = updateTileColor(_tileOneColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileTwoClicked() {
        if (_gameStarted.value!!) {
            _tileTwoColor.value = updateTileColor(_tileTwoColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileThreeClicked() {
        if (_gameStarted.value!!) {
            _tileThreeColor.value = updateTileColor(_tileThreeColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileFourClicked() {
        if (_gameStarted.value!!) {
            _tileFourColor.value = updateTileColor(_tileFourColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileFiveClicked() {
        if (_gameStarted.value!!) {
            _tileFiveColor.value = updateTileColor(_tileFiveColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileSixClicked() {
        if (_gameStarted.value!!) {
            _tileSixColor.value = updateTileColor(_tileSixColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileSevenClicked() {
        if (_gameStarted.value!!) {
            _tileSevenColor.value = updateTileColor(_tileSevenColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileEightClicked() {
        if (_gameStarted.value!!) {
            _tileEightColor.value = updateTileColor(_tileEightColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileNineClicked() {
        if (_gameStarted.value!!) {
            _tileNineColor.value = updateTileColor(_tileNineColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileTenClicked() {
        if (_gameStarted.value!!) {
            _tileTenColor.value = updateTileColor(_tileTenColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileElevenClicked() {
        if (_gameStarted.value!!) {
            _tileElevenColor.value = updateTileColor(_tileElevenColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileTwelveClicked() {
        if (_gameStarted.value!!) {
            _tileTwelveColor.value = updateTileColor(_tileTwelveColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileThirteenClicked() {
        if (_gameStarted.value!!) {
            _tileThirteenColor.value = updateTileColor(_tileThirteenColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileFourteenClicked() {
        if (_gameStarted.value!!) {
            _tileFourteenColor.value = updateTileColor(_tileFourteenColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileFifteenClicked() {
        if (_gameStarted.value!!) {
            _tileFifteenColor.value = updateTileColor(_tileFifteenColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileSixteenClicked() {
        if (_gameStarted.value!!) {
            _tileSixteenColor.value = updateTileColor(_tileSixteenColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    private fun updateTileColor(tileColor: MutableLiveData<Int>): Int {
        var newTileColor: Int = tileColor.value!!
        // Timber.v("%s %s", "value of tileColor", tileColor.value)
        val randomInt = (0..2).random()
        // Timber.v("%s %s", "value of randomInt is", randomInt)
        // Timber.v("%s %s", "value of newTileColor is", newTileColor)
        if (!eventGameFinish.value!!) {
            if (tileColor.value == R.color.red) {
                when (randomInt) {
                    0 -> newTileColor = R.color.yellow
                    1 -> newTileColor = R.color.green
                    2 -> newTileColor = R.color.blue
                }
            }
            if (tileColor.value == R.color.blue) {
                when (randomInt) {
                    0 -> newTileColor = R.color.yellow
                    1 -> newTileColor = R.color.green
                    2 -> newTileColor = R.color.red
                }
            }
            if (tileColor.value == R.color.yellow) {
                when (randomInt) {
                    0 -> newTileColor = R.color.blue
                    1 -> newTileColor = R.color.green
                    2 -> newTileColor = R.color.red
                }
            }
            if (tileColor.value == R.color.green) {
                when (randomInt) {
                    0 -> newTileColor = R.color.blue
                    1 -> newTileColor = R.color.yellow
                    2 -> newTileColor = R.color.red
                }
            }
        }
        // Timber.v("%s %s", "value of R.color.yellow", R.color.yellow)
        // Timber.v("%s %s", "value of R.color.green", R.color.green)
        // Timber.v("%s %s", "value of R.color.red", R.color.red)
        // Timber.v("%s %s", "value of R.color.blue", R.color.blue)
        // Timber.v("%s %s", "value of newTileColor", newTileColor)
        _score.value = (_score.value)?.plus(1)
        // Timber.v("%s %s", "value of newTileColor is", newTileColor)
        return newTileColor
    }

    private fun isGameOver(level: GameDifficultyLevel?) {
        when (level) {
            GameDifficultyLevel.EASY -> { // board is 2x2
                if (_tileOneColor.value == _tileTwoColor.value
                        && _tileTwoColor.value == _tileThreeColor.value
                        && _tileThreeColor.value == _tileFourColor.value) {
                    _winner.value = true
                    _eventGameFinish.value = true
                }
            }
            GameDifficultyLevel.MEDIUM -> { // board is 3x3
                if (_tileOneColor.value == _tileTwoColor.value
                        && _tileTwoColor.value == _tileThreeColor.value
                        && _tileThreeColor.value == _tileFourColor.value
                        && _tileFourColor.value == _tileFiveColor.value
                        && _tileFiveColor.value == _tileSixColor.value
                        && _tileSixColor.value == _tileSevenColor.value
                        && _tileSevenColor.value == _tileEightColor.value
                        && _tileEightColor.value == _tileNineColor.value) {
                    _winner.value = true
                    _eventGameFinish.value = true
                }
            }
            GameDifficultyLevel.HARD -> { // board is 4x4
                if (_tileOneColor.value == _tileTwoColor.value
                    && _tileTwoColor.value == _tileThreeColor.value
                    && _tileThreeColor.value == _tileFourColor.value
                    && _tileFourColor.value == _tileFiveColor.value
                    && _tileFiveColor.value == _tileSixColor.value
                    && _tileSixColor.value == _tileSevenColor.value
                    && _tileSevenColor.value == _tileEightColor.value
                    && _tileEightColor.value == _tileNineColor.value
                    && _tileNineColor.value == _tileTenColor.value
                    && _tileTenColor.value == _tileElevenColor.value
                    && _tileElevenColor.value == _tileTwelveColor.value
                    && _tileTwelveColor.value == _tileThirteenColor.value
                    && _tileThirteenColor.value == _tileFourteenColor.value
                ) {
                    _winner.value = true
                    _eventGameFinish.value = true
                }
            }
            else -> {}
        }
    }

    /** Methods for completed events **/
    fun onGameFinishComplete(difficultyLevel: String) {
        _winner.value = false
        _eventGameFinish.value = false
        _gameStarted.value = false
        _score.value = 0
        _currentTime.value = DONE
        timer.cancel()

        when (difficultyLevel) {
            "Easy" -> {
                _gameDifficultyLevel.value = GameDifficultyLevel.EASY
                setup2x2()
            }
            "Medium" -> {
                _gameDifficultyLevel.value = GameDifficultyLevel.MEDIUM
                setup3x3()
            }
            "Hard" -> {
                _gameDifficultyLevel.value = GameDifficultyLevel.HARD
                setup4x4()
            }
            else -> Timber.v("value of BAD")
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}