package com.axel.reproductorenkotlin.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.axel.reproductorenkotlin.databinding.FragmentPlaylistBinding

class PlaylistsFragment: Fragment() {

    private var fragmentPlaylistBinding: FragmentPlaylistBinding? = null
    private val binding get() = fragmentPlaylistBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentPlaylistBinding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentPlaylistBinding = null
    }
}