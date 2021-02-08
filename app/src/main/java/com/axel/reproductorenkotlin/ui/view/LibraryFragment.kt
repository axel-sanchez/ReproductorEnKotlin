package com.axel.reproductorenkotlin.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.common.hide
import com.axel.reproductorenkotlin.common.show
import com.axel.reproductorenkotlin.data.models.UserPlaylists
import com.axel.reproductorenkotlin.databinding.FragmentLibraryBinding
import com.axel.reproductorenkotlin.domain.usecase.GetUserPlaylistsUseCase
import com.axel.reproductorenkotlin.ui.view.adapter.LibraryAdapter
import com.axel.reproductorenkotlin.viewmodel.LibraryViewModel
import org.koin.android.ext.android.inject

class LibraryFragment : Fragment() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val getUserPlaylistsUseCase: GetUserPlaylistsUseCase by inject()

    private val viewModel: LibraryViewModel by activityViewModels(
        factoryProducer = { LibraryViewModel.LibraryViewModelFactory(getUserPlaylistsUseCase) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.hide()
        binding.progress.playAnimation()

        setUpObserverUserPlaylists()
    }

    private fun setUpObserverUserPlaylists() {
        viewModel.getUserPlaylistsLiveData().observe(viewLifecycleOwner, {
            binding.progress.cancelAnimation()
            binding.progress.hide()
            binding.recyclerView.show()
            setAdapter(it)
        })
    }

    private var fragmentLibraryBinding: FragmentLibraryBinding? = null
    private val binding get() = fragmentLibraryBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentLibraryBinding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentLibraryBinding = null
    }

    private fun setAdapter(playlists: List<UserPlaylists.Item?>) {
        viewAdapter = LibraryAdapter(playlists) { itemClick(it) }

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

    private fun itemClick(playlist: UserPlaylists.Item?) {
        val action = LibraryFragmentDirections.toSongsFragment(idPlaylist = playlist?.id?:"", isSearch = false)
        findNavController().navigate(action)
    }
}