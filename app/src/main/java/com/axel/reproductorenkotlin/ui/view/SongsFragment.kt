package com.axel.reproductorenkotlin.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout.VERTICAL
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.common.hide
import com.axel.reproductorenkotlin.common.show
import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.databinding.FragmentSongsBinding
import com.axel.reproductorenkotlin.domain.SongsUseCase
import com.axel.reproductorenkotlin.ui.view.adapter.TrackAdapter
import com.axel.reproductorenkotlin.viewmodel.SongsViewModel
import org.koin.android.ext.android.inject

class SongsFragment : Fragment() {

    private var idPlaylist = ""

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val songsUseCase: SongsUseCase by inject()

    private val viewModel: SongsViewModel by viewModels(
        factoryProducer = { SongsViewModel.SongsViewModelFactory(songsUseCase) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idPlaylist = SongsFragmentArgs.fromBundle(requireArguments()).idPlaylist
    }

    private var fragmentSongsBinding: FragmentSongsBinding? = null
    private val binding get() = fragmentSongsBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentSongsBinding = FragmentSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progress.playAnimation()
        binding.recyclerView.hide()

        setUpObserveSongs()

        viewModel.getPlaylistSongs(idPlaylist)
    }

    private fun setUpObserveSongs() {
        viewModel.getPlaylistSongsLiveData().observe(viewLifecycleOwner, {
            binding.progress.cancelAnimation()
            binding.progress.hide()
            binding.recyclerView.show()

            it?.let {
                setAdapter(it)
            }
        })
    }

    private fun setAdapter(songs: List<PlaylistSongs.Item.Track?>) {
        viewAdapter = TrackAdapter(songs, lifecycleScope) { spotifyClickListener(it) }

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

    private fun spotifyClickListener(song: PlaylistSongs.Item.Track?) {
        val intent = Intent(Intent.ACTION_VIEW)
        //intent.data = Uri.parse("spotify:album:0sNOF9WDwhWunNAHPD3Baj")
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
    }
}