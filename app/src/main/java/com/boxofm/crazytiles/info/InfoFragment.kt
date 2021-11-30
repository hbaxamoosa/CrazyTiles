package com.boxofm.crazytiles.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.boxofm.crazytiles.R
import com.boxofm.crazytiles.database.GamesDatabase
import com.boxofm.crazytiles.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding
    private lateinit var viewModel: InfoViewModel
    private lateinit var viewModelFactory: InfoViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)

        val dataSource = GamesDatabase.getInstance(requireContext()).gamesDatabaseDao
        viewModelFactory = InfoViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(InfoViewModel::class.java)


        // Bind the activity to the ViewModel
        binding.infoViewModel = viewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        viewModel.gameReset.observe(viewLifecycleOwner, Observer { resetGame ->
            if (resetGame) {
                // Timber.v("%s %s", "game reset requested. Value of resetGame is ", resetGame)
                viewModel.resetStatsComplete()
            }
        })

        return binding.root
    }
}