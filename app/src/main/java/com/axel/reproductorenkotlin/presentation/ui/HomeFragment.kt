package com.axel.reproductorenkotlin.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.Song
import com.axel.reproductorenkotlin.databinding.FragmentHomeBinding
import com.axel.reproductorenkotlin.helpers.SongHelper.songsList
import com.axel.reproductorenkotlin.presentation.adapter.SongAdapter
import com.axel.reproductorenkotlin.presentation.viewmodel.PlayerViewModel
import java.lang.ref.WeakReference

class HomeFragment : Fragment() {

    private val viewModelPlayer: PlayerViewModel by activityViewModels(
        factoryProducer = { PlayerViewModel.PlayerViewModelFactory(WeakReference(requireActivity().applicationContext)) }
    )

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private val binding get() = fragmentHomeBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHomeBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
    }

    private fun setAdapter() {
        viewAdapter = SongAdapter(songsList) { itemClick(it) }

        viewManager = GridLayoutManager(this.requireContext(), 2)

        binding.recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    private fun itemClick(song: Song) {
        prepareSongSelected(song)
        MainFragment.mainFragmentInstance.findNavController().navigate(R.id.toPlayerFragment)
    }

    private fun prepareSongSelected(song: Song) {
        if (viewModelPlayer.mediaPlayerIsPlaying()) {
            viewModelPlayer.getTime().iteratorSeconds = 0.0f

            viewModelPlayer.resetAndPrepareMediaPlayer()

            viewModelPlayer.setPosition(songsList.indexOf(song))
            viewModelPlayer.initializeMediaPlayer()
            viewModelPlayer.getTime().minuteMilliSeconds = 0.0f
            viewModelPlayer.getTime().durationMilliSeconds = viewModelPlayer.getSongDuration()
            viewModelPlayer.playSong()
        } else {
            viewModelPlayer.getTime().iteratorSeconds = 0.0f
            viewModelPlayer.setPosition(songsList.indexOf(song))
            viewModelPlayer.initializeMediaPlayer()
            viewModelPlayer.getTime().minuteMilliSeconds = 0.0f
            viewModelPlayer.getTime().durationMilliSeconds = viewModelPlayer.getSongDuration()
            viewModelPlayer.setCanExecute(true)
            viewModelPlayer.playSong()
            viewModelPlayer.job?.cancel()
            viewModelPlayer.runSong()
        }
    }
}