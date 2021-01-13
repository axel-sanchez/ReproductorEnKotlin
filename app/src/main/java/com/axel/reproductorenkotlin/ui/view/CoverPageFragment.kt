package com.axel.reproductorenkotlin.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.axel.reproductorenkotlin.data.models.Song
import com.axel.reproductorenkotlin.databinding.FragmentCoverPageBinding
import com.axel.reproductorenkotlin.ui.view.customs.ReproductorFragment

const val ARG_POS = "pos"

class CoverPageFragment: ReproductorFragment() {

    var position = 0
    lateinit var canciones: MutableList<Song>

    override fun onBackPressFragment() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POS)
        }
    }

    private var fragmentCoverPageBinding: FragmentCoverPageBinding? = null
    private val binding get() = fragmentCoverPageBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentCoverPageBinding = FragmentCoverPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentCoverPageBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setImageResource(canciones[position].coverPage)
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int, canciones: MutableList<Song>) =
            CoverPageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POS, position)
                }
                this.canciones = canciones
            }
    }
}