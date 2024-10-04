package com.axel.reproductorenkotlin.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.databinding.FragmentMainBinding
import com.axel.reproductorenkotlin.helpers.SongHelper
import com.axel.reproductorenkotlin.presentation.viewmodel.PlayerViewModel
import java.lang.ref.WeakReference

class MainFragment : Fragment() {

    private val viewModelPlayer: PlayerViewModel by activityViewModels(
        factoryProducer = { PlayerViewModel.PlayerViewModelFactory(WeakReference(requireActivity().applicationContext)) }
    )

    private var fragmentMainBinding: FragmentMainBinding? = null
    private val binding get() = fragmentMainBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentMainBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainFragmentInstance = this

        val navController = requireActivity().findNavController(R.id.fragment2)
        binding.bottomNavigationView.setupWithNavController(navController)

        setUpListenerPlayer()

        updateTextInBtnPlayer(SongHelper.songsList[viewModelPlayer.getPosition()].name)
    }

    private fun setUpListenerPlayer() {
        binding.btnPlayer.setOnClickListener {
            findNavController().navigate(R.id.toPlayerFragment)
        }
    }

    private fun updateTextInBtnPlayer(name: String){
        binding.btnPlayer.text = name
    }

    companion object{
        lateinit var mainFragmentInstance: MainFragment
    }
}