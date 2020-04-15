package com.axel.reproductorenkotlin.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.Cancion
import com.axel.reproductorenkotlin.ui.view.adapter.CancionAdapter
import com.axel.reproductorenkotlin.ui.view.customs.ReproductorFragment
import com.axel.reproductorenkotlin.ui.view.interfaces.IOnBackPressFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: ReproductorFragment() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var canciones: MutableList<Cancion>

    override fun OnBackPressFragment() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        canciones = mutableListOf()

        canciones.add(
            Cancion(R.raw.edsheerancastleonthehill, R.drawable.castleonthehill, "Castle on the hill")
        )
        canciones.add(
            Cancion(R.raw.edsheerandive, R.drawable.dive, "Dive")
        )
        canciones.add(
            Cancion(R.raw.edsheerangalwaygirl, R.drawable.galwaygirl, "Galway girl")
        )
        canciones.add(
            Cancion(R.raw.edsheerangivemelove, R.drawable.givemelove, "Give me love")
        )
        canciones.add(
            Cancion(R.raw.edsheeranhowwouldyoufeel, R.drawable.howwouldyoufeel, "How would you feel")
        )
        canciones.add(
            Cancion(R.raw.edsheeranlegohouse, R.drawable.legohouse, "Lego house")
        )
        canciones.add(
            Cancion(R.raw.edsheeranone, R.drawable.one, "One")
        )
        canciones.add(
            Cancion(R.raw.edsheeranperfect, R.drawable.perfect, "Perfect")
        )
        canciones.add(
            Cancion(R.raw.edsheerantheateam, R.drawable.theateam, "The a team")
        )
        canciones.add(
            Cancion(R.raw.edsheeranthinkingoutloud, R.drawable.thinkingoutloud, "Thinking out loud")
        )
        canciones.add(
            Cancion(R.raw.happieredsheeranlyric, R.drawable.happier, "Happier")
        )
        canciones.add(
            Cancion(R.raw.photographedsheeran, R.drawable.photograph, "Photograph")
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
    }

    private fun setAdapter() {
        viewAdapter = CancionAdapter(activity!!, canciones) { itemClick(it) }

        viewManager = GridLayoutManager(this.requireContext(), 2)

        recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    private fun itemClick(cancion: Cancion){
        Toast.makeText(context, "presiono la canción ${cancion.getNombre()}", Toast.LENGTH_SHORT).show()
    }
}