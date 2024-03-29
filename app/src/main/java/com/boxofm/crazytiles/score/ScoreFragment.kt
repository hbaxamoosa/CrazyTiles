package com.boxofm.crazytiles.score

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.boxofm.crazytiles.R
import com.boxofm.crazytiles.database.GamesDatabase
import com.boxofm.crazytiles.databinding.ScoreFragmentBinding

class ScoreFragment : Fragment() {
    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class.
        val binding: ScoreFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.score_fragment,
            container,
            false
        )

        val args: ScoreFragmentArgs by navArgs()
        val application = requireNotNull(this.activity).application
        val dataSource = GamesDatabase.getInstance(application).gamesDatabaseDao
        val sharesPrefs: SharedPreferences? = activity?.let {
            PreferenceManager.getDefaultSharedPreferences(
                it
            )
        }
        val difficultyLevel: String = sharesPrefs?.getString("list_preference", "unknown")!!

        viewModelFactory = ScoreViewModelFactory(args.score, args.winner, difficultyLevel, dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[ScoreViewModel::class.java]

        binding.scoreViewModel = viewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Navigates back to title when button is pressed
        viewModel.eventPlayAgain.observe(viewLifecycleOwner) { playAgain ->
            if (playAgain) {
                findNavController().navigate(ScoreFragmentDirections.actionScoreFragmentToGameFragment())
                viewModel.onPlayAgainComplete()
            }
        }

        return binding.root
    }
}