package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding
import androidx.navigation.fragment.NavHostFragment.findNavController

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var gameViewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        gameViewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })

        gameViewModel.word.observe(viewLifecycleOwner, Observer { newWord ->
            binding.wordText.text = newWord
        })

        gameViewModel.currentTime.observe(viewLifecycleOwner, Observer { newTime ->
            binding.timerText.text = DateUtils.formatElapsedTime(newTime)
        })

        binding.correctButton.setOnClickListener {
            gameViewModel.onCorrect()
        }

        binding.skipButton.setOnClickListener {
            gameViewModel.onSkip()
        }

        gameViewModel.eventGameFinished.observe(viewLifecycleOwner, Observer { hasFinished ->
            if(hasFinished) {
                val currentScore = gameViewModel.score.value ?: 0
                val action = GameFragmentDirections.actionGameToScore()
                findNavController(this).navigate(action)
                gameViewModel.onGameFinishComplete()
            }
        })

        return binding.root
    }

}
