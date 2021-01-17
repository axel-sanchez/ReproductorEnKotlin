package com.axel.reproductorenkotlin.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.common.hide
import com.axel.reproductorenkotlin.common.show
import com.axel.reproductorenkotlin.data.models.ItemSong
import com.axel.reproductorenkotlin.data.models.UserPlaylists
import com.axel.reproductorenkotlin.databinding.FragmentExploreBinding
import com.axel.reproductorenkotlin.domain.ExploreUseCase
import com.axel.reproductorenkotlin.ui.view.adapter.ExploreAdapter
import com.axel.reproductorenkotlin.viewmodel.ExploreViewModel
import org.koin.android.ext.android.inject

class ExploreFragment : Fragment() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val exploreUseCase: ExploreUseCase by inject()
    private val viewModel: ExploreViewModel by activityViewModels(
        factoryProducer = { ExploreViewModel.ExploreViewModelFactory(exploreUseCase) }
    )

    private var fragmentExploreBinding: FragmentExploreBinding? = null
    private val binding get() = fragmentExploreBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentExploreBinding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentExploreBinding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.hide()
        binding.progress.playAnimation()

        setUpObserveViewModel()
    }

    private fun setUpObserveViewModel() {
        viewModel.getItemSongListLiveData().observe(viewLifecycleOwner, {
            binding.progress.cancelAnimation()
            binding.progress.hide()
            binding.recyclerView.show()
            setAdapter(it)
        })
    }

    private fun setAdapter(itemSongList: MutableList<ItemSong?>) {
        viewAdapter = ExploreAdapter(itemSongList) { itemClick(it) }

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

    private fun itemClick(playlist: ItemSong) {
        val bundle = bundleOf("idPlaylist" to playlist.getId())
        findNavController().navigate(R.id.toSongsFragment, bundle, null, null)
    }
}