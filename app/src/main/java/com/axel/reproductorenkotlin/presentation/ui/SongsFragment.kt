package com.axel.reproductorenkotlin.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout.VERTICAL
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.common.hide
import com.axel.reproductorenkotlin.common.show
import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.data.models.Search
import com.axel.reproductorenkotlin.databinding.FragmentSongsBinding
import com.axel.reproductorenkotlin.domain.usecase.GetPlaylistSongsUseCase
import com.axel.reproductorenkotlin.domain.usecase.GetSongsBySearchUseCase
import com.axel.reproductorenkotlin.presentation.adapter.PlaylistSongsAdapter
import com.axel.reproductorenkotlin.presentation.adapter.SearchedSongsAdapter
import com.axel.reproductorenkotlin.presentation.viewmodel.SongsViewModel
import org.koin.android.ext.android.inject

class SongsFragment : Fragment() {

    private var idPlaylist = ""
    private var query = ""
    private var isSearch = false

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val getPlaylistSongsUseCase: GetPlaylistSongsUseCase by inject()
    private val getSongsBySearchUseCase: GetSongsBySearchUseCase by inject()

    private val viewModel: SongsViewModel by viewModels(
        factoryProducer = { SongsViewModel.SongsViewModelFactory(getPlaylistSongsUseCase, getSongsBySearchUseCase) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idPlaylist = SongsFragmentArgs.fromBundle(requireArguments()).idPlaylist
        query = SongsFragmentArgs.fromBundle(requireArguments()).query
        isSearch = SongsFragmentArgs.fromBundle(requireArguments()).isSearch
    }

    private var fragmentSongsBinding: FragmentSongsBinding? = null
    private val binding get() = fragmentSongsBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentSongsBinding = FragmentSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progress.playAnimation()
        binding.recyclerView.hide()

        if(isSearch){
            setUpObserveSongs()
            viewModel.getSongsByQuery(query)
        } else{
            setUpObservePlaylistSongs()
            viewModel.getPlaylistSongs(idPlaylist)
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setUpObserveSongs() {
        viewModel.getSongsByQueryLiveData().observe(viewLifecycleOwner, {
            binding.progress.cancelAnimation()
            binding.progress.hide()
            binding.recyclerView.show()

            it?.let {
                setAdapterSongsByQuery(it)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setAdapterSongsByQuery(songs: List<Search.Tracks.Item?>) {
        viewAdapter = SearchedSongsAdapter(songs, lifecycleScope) { spotifySearchedSongsClickListener(it) }

        viewManager = GridLayoutManager(this.requireContext(), 1)

        val dividerItemDecoration = DividerItemDecoration(requireContext(), VERTICAL)

        binding.recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            addItemDecoration(dividerItemDecoration)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setUpObservePlaylistSongs() {
        viewModel.getPlaylistSongsLiveData().observe(viewLifecycleOwner, {
            binding.progress.cancelAnimation()
            binding.progress.hide()
            binding.recyclerView.show()

            it?.let {
                setAdapterPlaylistSongs(it)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setAdapterPlaylistSongs(songs: List<PlaylistSongs.Item.Track?>) {
        viewAdapter = PlaylistSongsAdapter(songs, lifecycleScope) { spotifyPlaylistSongsClickListener(it) }

        viewManager = GridLayoutManager(this.requireContext(), 1)

        val dividerItemDecoration = DividerItemDecoration(requireContext(), VERTICAL)

        binding.recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            addItemDecoration(dividerItemDecoration)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun spotifyPlaylistSongsClickListener(song: PlaylistSongs.Item.Track?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(song?.uri)
        intent.putExtra(
            Intent.EXTRA_REFERRER,
            Uri.parse("android-app://" + requireActivity().packageName)
        )
        this.startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun spotifySearchedSongsClickListener(song: Search.Tracks.Item?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(song?.uri)
        intent.putExtra(
            Intent.EXTRA_REFERRER,
            Uri.parse("android-app://" + requireActivity().packageName)
        )
        this.startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentSongsBinding = null
        if(viewAdapter is PlaylistSongsAdapter) (viewAdapter as PlaylistSongsAdapter).destroyJob()
        if(viewAdapter is SearchedSongsAdapter) (viewAdapter as SearchedSongsAdapter).destroyJob()
    }
}