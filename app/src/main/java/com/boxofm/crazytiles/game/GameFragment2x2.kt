package com.boxofm.crazytiles.game

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.boxofm.crazytiles.R
import com.boxofm.crazytiles.databinding.FragmentGame2x2Binding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import timber.log.Timber

class GameFragment2x2 : Fragment() {

    private lateinit var viewModelFactory: GameViewModelFactory
    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGame2x2Binding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var remoteConfig: FirebaseRemoteConfig


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val time: Long
        val sharedPrefs: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(activity)

        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())

        // Firebase Remote Config
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(Utils.getCacheExpiration())
                .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        time = Utils.fetchRemoteConfigValues(remoteConfig, sharedPrefs.getString("list_preference", "unknown")!!)

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_2x2, container, false
        )

        viewModelFactory = GameViewModelFactory(sharedPrefs.getString("list_preference", "unknown")!!, time)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access to all the data in the VieWModel
        binding.gameViewModel = viewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        // Sets up an Observer to react to the game difficulty level selection
        val navController = findNavController()
        viewModel.gameDifficultyLevel.observe(viewLifecycleOwner, Observer {
            when (it) {
                GameViewModel.GameDifficultyLevel.EASY -> {
                    // Firebase Analytics
                    val bundle = Bundle()
                    bundle.putString("level", sharedPrefs.getString("list_preference", "unknown")!!)
                    firebaseAnalytics.logEvent("game_difficulty", bundle)
                }
                GameViewModel.GameDifficultyLevel.MEDIUM -> {
                    navController.navigate(R.id.action_gameFragment2x2_to_gameFragment3x3)
                }
                GameViewModel.GameDifficultyLevel.HARD -> {
                    navController.navigate(R.id.action_gameFragment2x2_to_gameFragment4x4)
                }
                else -> {
                    Timber.v("%s %s", "something BAD is ", "happening")
                }
            }
        })

        // Sets up event listening to navigate the player when the game is finished
        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { isFinished ->
            if (isFinished) {
                val currentScore = viewModel.score.value ?: 0
                /*Timber.v("%s %s", "value of currentScore is ", currentScore)*/
                val winner = viewModel.winner.value
                /*Timber.v("%s %s", "value of winner is ", winner)*/
                val action = GameFragment2x2Directions.actionGameFragment2x2ToScoreFragment(currentScore, winner!!)
                navController.navigate(action)
                viewModel.onGameFinishComplete()
            }
        })

        // Hides the 'Start Game' button after the game has started
        viewModel.gameStarted.observe(viewLifecycleOwner, Observer { gameStarted ->
            if (gameStarted) {
                binding.buttonPlay.visibility = View.INVISIBLE
                binding.imageViewHelp.visibility = View.INVISIBLE
            } else {
                binding.buttonPlay.visibility = View.VISIBLE
                binding.imageViewHelp.visibility = View.VISIBLE
            }
        })

        binding.imageViewHelp.setOnClickListener {
            navController.navigate(GameFragment2x2Directions.actionGameFragment2x2ToInfoFragment())
        }

        return binding.root
    }
}