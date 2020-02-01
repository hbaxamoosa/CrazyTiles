package com.boxofm.crazytiles.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.boxofm.crazytiles.R
import timber.log.Timber


private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

/**
 * ViewModel containing all the logic needed to run the game
 */
class GameViewModel(difficultyLevel: String) : ViewModel() {
    // These are the three different types of buzzing in the game. Buzz pattern is the number of
    // milliseconds each interval of buzzing and non-buzzing takes.
    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    enum class GameDifficultyLevel {
        EASY,
        MEDIUM,
        HARD
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
        private const val COUNTDOWN_TIME = 5000L

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
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
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

    // Event that triggers the phone to buzz using different patterns, determined by BuzzType
    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz


    private var gameStarted: Boolean
    val gameDifficultyLevel = MutableLiveData<GameDifficultyLevel>()

    init {

        Timber.v("%s %s", "value of game difficulty is ", difficultyLevel)
        _score.value = 0
        _winner.value = false
        _eventGameFinish.value = false
        gameStarted = false

        // set tile colors
        when (difficultyLevel) {
            "Easy" -> {
                gameDifficultyLevel.value = GameDifficultyLevel.EASY
                setup2x2()
            }
            "Medium" -> {
                gameDifficultyLevel.value = GameDifficultyLevel.MEDIUM
                setup3x3()
            }
            "Hard" -> {
                gameDifficultyLevel.value = GameDifficultyLevel.HARD
                setup4x4()
            }
            else -> Timber.v("value of BAD")
        }

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
    }

    fun startGame() {
        gameStarted = true
        timer.start()
    }

    fun setup2x2() {
        _tileOneColor.value = R.color.blue
        _tileTwoColor.value = R.color.blue
        _tileThreeColor.value = R.color.blue
        _tileFourColor.value = R.color.red
    }

    fun setup3x3() {
        _tileOneColor.value = R.color.blue
        _tileTwoColor.value = R.color.yellow
        _tileThreeColor.value = R.color.green
        _tileFourColor.value = R.color.red
        _tileFiveColor.value = R.color.blue
        _tileSixColor.value = R.color.yellow
        _tileSevenColor.value = R.color.green
        _tileEightColor.value = R.color.blue
        _tileNineColor.value = R.color.red
    }

    fun setup4x4() {
        _tileOneColor.value = R.color.blue
        _tileTwoColor.value = R.color.yellow
        _tileThreeColor.value = R.color.green
        _tileFourColor.value = R.color.red
        _tileFiveColor.value = R.color.blue
        _tileSixColor.value = R.color.yellow
        _tileSevenColor.value = R.color.green
        _tileEightColor.value = R.color.red
        _tileNineColor.value = R.color.red
        _tileTenColor.value = R.color.yellow
        _tileElevenColor.value = R.color.green
        _tileTwelveColor.value = R.color.red
        _tileThirteenColor.value = R.color.red
        _tileFourteenColor.value = R.color.green
        _tileFifteenColor.value = R.color.red
        _tileSixteenColor.value = R.color.red
    }

    /** Methods for buttons presses **/
    fun tileOneClicked() {
        if (gameStarted) {
            _tileOneColor.value = updateTileColor(_tileOneColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileTwoClicked() {
        if (gameStarted) {
            _tileTwoColor.value = updateTileColor(_tileTwoColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileThreeClicked() {
        if (gameStarted) {
            _tileThreeColor.value = updateTileColor(_tileThreeColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileFourClicked() {
        if (gameStarted) {
            _tileFourColor.value = updateTileColor(_tileFourColor)
            isGameOver(gameDifficultyLevel.value)
        } else {
            Timber.v("%s %s", "game has not started yet. ", "click the start button!!")
        }
    }

    fun tileFiveClicked() {
        if (gameStarted) {
            _tileFiveColor.value = updateTileColor(_tileFiveColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileSixClicked() {
        if (gameStarted) {
            _tileSixColor.value = updateTileColor(_tileSixColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileSevenClicked() {
        if (gameStarted) {
            _tileSevenColor.value = updateTileColor(_tileSevenColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileEightClicked() {
        if (gameStarted) {
            _tileEightColor.value = updateTileColor(_tileEightColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileNineClicked() {
        if (gameStarted) {
            _tileNineColor.value = updateTileColor(_tileNineColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileTenClicked() {
        if (gameStarted) {
            _tileTenColor.value = updateTileColor(_tileTenColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileElevenClicked() {
        if (gameStarted) {
            _tileElevenColor.value = updateTileColor(_tileElevenColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileTwelveClicked() {
        if (gameStarted) {
            _tileTwelveColor.value = updateTileColor(_tileTwelveColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileThirteenClicked() {
        if (gameStarted) {
            _tileThirteenColor.value = updateTileColor(_tileThirteenColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileFourteenClicked() {
        if (gameStarted) {
            _tileFourteenColor.value = updateTileColor(_tileFourteenColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileFifteenClicked() {
        if (gameStarted) {
            _tileFifteenColor.value = updateTileColor(_tileFifteenColor)
            isGameOver(gameDifficultyLevel.value)
        }
    }

    fun tileSixteenClicked() {
        if (gameStarted) {
            _tileSixteenColor.value = updateTileColor(_tileSixteenColor)
            isGameOver(gameDifficultyLevel.value)
        }
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
                        && _tileThirteenColor.value == _tileFourteenColor.value) {
                    _winner.value = true
                    _eventGameFinish.value = true
                }
            }
        }

    }

    /** Methods for completed events **/

    fun onGameFinishComplete() {
        _winner.value = false
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