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
import com.boxofm.crazytiles.databinding.FragmentGame3x3Binding
import timber.log.Timber

class GameFragment3x3 : Fragment() {

    private lateinit var viewModelFactory: GameViewModelFactory
    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGame3x3Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val sharedPref: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(activity)

        when (sharedPref.getString("list_preference", "unknown")) {
            ("Easy") -> {
                Timber.v("%s %s", "game level is ", "Easy")
                findNavController().navigate(R.id.gameFragment2x2_destination)
            }
            ("Medium") -> {
                Timber.v("%s %s", "game level is ", "Medium")
            }
            ("Hard") -> {
                Timber.v("%s %s", "game level is ", "Hard")
            }
            else -> Timber.v("%s %s", "game level is ", "unknown")
        }

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_3x3, container, false
        )

        viewModelFactory = GameViewModelFactory(sharedPref.getString("list_preference", "unknown")!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access to all of the
        // data in the VieWModel
        binding.gameViewModel = viewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        // Sets up event listening to navigate the player when the game is finished
        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { isFinished ->
            if (isFinished) {
                val currentScore = viewModel.score.value ?: 0
                val action = GameFragment3x3Directions.actionGameFragment3x3ToScoreDestination(currentScore)
                findNavController().navigate(action)
                viewModel.onGameFinishComplete()
            }
        })

        return binding.root
    }
}