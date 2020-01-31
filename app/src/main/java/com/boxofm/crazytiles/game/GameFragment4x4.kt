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
import com.boxofm.crazytiles.databinding.FragmentGame4x4Binding

class GameFragment4x4 : Fragment() {

    private lateinit var viewModelFactory: GameViewModelFactory
    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGame4x4Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val sharedPref: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(activity)

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_4x4, container, false
        )

        viewModelFactory = GameViewModelFactory(sharedPref.getString("list_preference", "unknown")!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access to all of the
        // data in the VieWModel
        binding.gameViewModel = viewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        // Sets up an Observer to react to the game difficulty level selection
        val navController = findNavController()
        viewModel.gameDifficultyLevel.observe(viewLifecycleOwner, Observer { level ->
            /*Timber.v("%s %s", "value of level is ", level)*/
            when (level) {
                GameViewModel.GameDifficultyLevel.EASY -> {
                    navController.navigate(R.id.gameFragment2x2_destination)
                }
                GameViewModel.GameDifficultyLevel.MEDIUM -> {
                    navController.navigate(R.id.gameFragment3x3_destination)
                }
                GameViewModel.GameDifficultyLevel.HARD -> {
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
                val action = GameFragment4x4Directions.actionGameFragment4x4ToScoreFragment(currentScore, winner!!)
                findNavController().navigate(action)
                viewModel.onGameFinishComplete()
            }
        })

        return binding.root
    }
}