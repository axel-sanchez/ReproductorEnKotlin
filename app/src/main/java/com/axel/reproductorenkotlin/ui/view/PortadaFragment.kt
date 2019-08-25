package com.axel.reproductorenkotlin.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.Cancion
import com.axel.reproductorenkotlin.ui.view.customs.ReproductorFragment
import kotlinx.android.synthetic.main.fragment_portada.*

const val ARG_POS = "pos"

class PortadaFragment: ReproductorFragment() {

    var position = 0
    lateinit var canciones: MutableList<Cancion>

    override fun OnBackPressFragment() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_portada, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView.setImageResource(canciones.get(position).getPortada())

    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int, canciones: MutableList<Cancion>) =
            PortadaFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POS, position)
                }
                this.canciones = canciones
            }
    }

}