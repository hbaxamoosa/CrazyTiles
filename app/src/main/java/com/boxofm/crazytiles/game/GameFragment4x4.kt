package com.boxofm.crazytiles.game

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.boxofm.crazytiles.R
import com.boxofm.crazytiles.databinding.FragmentGame4x4Binding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import timber.log.Timber

class GameFragment4x4 : Fragment() {

    private lateinit var viewModelFactory: GameViewModelFactory
    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGame4x4Binding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val time: Long

        sharedPrefs = activity?.let { PreferenceManager.getDefaultSharedPreferences(it) }!!

        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())

        // NavController
        navController = findNavController()

        // Firebase Remote Config
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

        // New settings builder syntax
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = Utils.getCacheExpiration()
            fetchTimeoutInSeconds = 60
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        val difficultyLevel: String = sharedPrefs.getString("list_preference", "unknown")!!
        time = Utils.fetchRemoteConfigValues(remoteConfig, difficultyLevel)

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_4x4, container, false
        )

        viewModelFactory = GameViewModelFactory(sharedPrefs.getString("list_preference", "unknown")!!, time)
        viewModel = ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]

        // Set the viewmodel for databinding - this allows the bound layout access to all of the
        // data in the VieWModel
        binding.gameViewModel = viewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Sets up event listening to navigate the player when the game is finished
        viewModel.eventGameFinish.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) {
                val currentScore = viewModel.score.value ?: 0
                /*Timber.v("%s %s", "value of currentScore is ", currentScore)*/
                val winner = viewModel.winner.value
                /*Timber.v("%s %s", "value of winner is ", winner)*/
                val action = GameFragment4x4Directions.actionGameFragment4x4ToScoreFragment(
                    currentScore,
                    winner!!
                )
                findNavController().navigate(action)
                viewModel.onGameFinishComplete(difficultyLevel)
            }
        }

        // Hides the 'Start Game' and 'Help' button after the game has started
        viewModel.gameStarted.observe(viewLifecycleOwner) { gameStarted ->
            if (gameStarted) {
                binding.timerText.visibility = View.VISIBLE
                binding.buttonPlay.visibility = View.INVISIBLE
                binding.imageViewHelp.visibility = View.INVISIBLE
            } else {
                binding.timerText.visibility = View.INVISIBLE
                binding.buttonPlay.visibility = View.VISIBLE
                binding.imageViewHelp.visibility = View.VISIBLE
            }
        }

        binding.imageViewHelp.setOnClickListener {
            navController.navigate(GameFragment4x4Directions.actionGameFragment4x4ToInfoFragment())
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Timber.v("%s %s", "inside onStart() value of difficultyLevel", difficultyLevel)

        when (val difficultyLevel: String = sharedPrefs.getString("list_preference", "unknown")!!) {
            "Easy" -> {
                findNavController().navigate(R.id.action_gameFragment4x4_to_gameFragment2x2)
            }
            "Medium" -> {
                findNavController().navigate(R.id.action_gameFragment4x4_to_gameFragment3x3)
            }
            "Hard" -> {
                // Firebase Analytics
                val bundle = Bundle()
                bundle.putString("level", difficultyLevel)
                firebaseAnalytics.logEvent("game_difficulty", bundle)
            }
            else -> {
                Timber.v("%s %s", "something BAD is ", "happening")
            }
        }
    }
}