package com.boxofm.crazytiles.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.boxofm.crazytiles.R


private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

/**
 * ViewModel containing all the logic needed to run the game
 */
class GameViewModel : ViewModel() {
    // These are the three different types of buzzing in the game. Buzz pattern is the number of
    // milliseconds each interval of buzzing and non-buzzing takes.
    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        // These represent different important times in the game, such as game length.

        // This is when the game is over
        private const val DONE = 0L

        // This is the time when the phone will start buzzing each second
        private const val COUNTDOWN_PANIC_SECONDS = 10L

        // This is the number of milliseconds in a second
        private const val ONE_SECOND = 1000L

        // This is the total time of the game
        private const val COUNTDOWN_TIME = 10000L

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

    private val timer: CountDownTimer

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    // The String version of the current time
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // Event which triggers the end of the game
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    // Event that triggers the phone to buzz using different patterns, determined by BuzzType
    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    init {
        _score.value = 0
        _eventGameFinish.value = false

        // set tile colors
        _tileOneColor.value = R.color.blue
        _tileTwoColor.value = R.color.yellow
        _tileThreeColor.value = R.color.green
        _tileFourColor.value = R.color.red

        // Creates a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventBuzz.value = BuzzType.GAME_OVER
                _eventGameFinish.value = true
            }
        }

        timer.start()
    }

    /** Methods for buttons presses **/

    fun tileOneClicked() {
        _tileOneColor.value = updateTileColor(_tileOneColor)
        isGameOver()
    }

    fun tileTwoClicked() {
        _tileTwoColor.value = updateTileColor(_tileTwoColor)
        isGameOver()
    }

    fun tileThreeClicked() {
        _tileThreeColor.value = updateTileColor(_tileThreeColor)
        isGameOver()
    }

    fun tileFourClicked() {
        _tileFourColor.value = updateTileColor(_tileFourColor)
        isGameOver()
    }

    fun updateTileColor(tileColor: MutableLiveData<Int>): Int {
        var newTileColor: Int = tileColor.value!!
        // Timber.v("%s %s", "value of eventGameFinished", eventGameFinish.value)
        val randomInt = (0..3).random()
        // Timber.v("%s %s", "value of randomInt is", randomInt)
        // Timber.v("%s %s", "value of newTileColor is", newTileColor)
        if (!eventGameFinish.value!!) {
            when (randomInt) {
                0 -> newTileColor = R.color.yellow
                1 -> newTileColor = R.color.green
                2 -> newTileColor = R.color.red
                3 -> newTileColor = R.color.blue
            }
        }
        _score.value = (_score.value)?.plus(1)
        // Timber.v("%s %s", "value of newTileColor is", newTileColor)
        return newTileColor
    }

    private fun isGameOver() {
        if (_tileOneColor.value == _tileTwoColor.value
                && _tileTwoColor.value == _tileThreeColor.value
                && _tileThreeColor.value == _tileFourColor.value) {
            // Timber.v("%s %s", "All tiles are the same color", "GAME OVER")
            _eventGameFinish.value = true
        }
    }

    /** Methods for completed events **/

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}