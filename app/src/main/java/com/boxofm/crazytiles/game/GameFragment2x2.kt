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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.boxofm.crazytiles.R
import com.boxofm.crazytiles.databinding.FragmentGame2x2Binding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import timber.log.Timber

class GameFragment2x2 : Fragment() {

    private lateinit var viewModelFactory: GameViewModelFactory
    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGame2x2Binding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val time: Long
        val difficultyLevel: String

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity)

        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())

        // NavController
        navController = findNavController()

        // Firebase Remote Config
        val remoteConfig = Firebase.remoteConfig

        // New settings builder syntax
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = Utils.getCacheExpiration()
            fetchTimeoutInSeconds = 60
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        difficultyLevel = sharedPrefs.getString("list_preference", "unknown")!!
        time = Utils.fetchRemoteConfigValues(remoteConfig, difficultyLevel)

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_2x2, container, false
        )

        viewModelFactory = GameViewModelFactory(difficultyLevel, time)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access to all the data in the VieWModel
        binding.gameViewModel = viewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

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

        // Hides the 'Start Game' and 'Help' button after the game has started
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

    override fun onStart() {
        super.onStart()
        val difficultyLevel: String = sharedPrefs.getString("list_preference", "unknown")!!
        // Timber.v("%s %s", "inside onStart() value of difficultyLevel", difficultyLevel)

        when (difficultyLevel) {
            "Easy" -> {
                // Firebase Analytics
                val bundle = Bundle()
                bundle.putString("level", difficultyLevel)
                firebaseAnalytics.logEvent("game_difficulty", bundle)
            }
            "Medium" -> {
                findNavController().navigate(R.id.action_gameFragment2x2_to_gameFragment3x3)
            }
            "Hard" -> {
                findNavController().navigate(R.id.action_gameFragment2x2_to_gameFragment4x4)
            }
            else -> {
                Timber.v("%s %s", "something BAD is ", "happening")
            }
        }
    }
}