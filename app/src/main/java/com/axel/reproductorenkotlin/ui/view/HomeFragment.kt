package com.axel.reproductorenkotlin.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.Song
import com.axel.reproductorenkotlin.databinding.FragmentHomeBinding
import com.axel.reproductorenkotlin.ui.view.adapter.SongAdapter
import com.axel.reproductorenkotlin.ui.view.customs.ReproductorFragment

class HomeFragment: ReproductorFragment() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var canciones: MutableList<Song>

    override fun onBackPressFragment() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        canciones = mutableListOf()

        canciones.add(
            Song(R.raw.edsheerancastleonthehill, R.drawable.castleonthehill, "Castle on the hill")
        )
        canciones.add(
            Song(R.raw.edsheerandive, R.drawable.dive, "Dive")
        )
        canciones.add(
            Song(R.raw.edsheerangalwaygirl, R.drawable.galwaygirl, "Galway girl")
        )
        canciones.add(
            Song(R.raw.edsheerangivemelove, R.drawable.givemelove, "Give me love")
        )
        canciones.add(
            Song(R.raw.edsheeranhowwouldyoufeel, R.drawable.howwouldyoufeel, "How would you feel")
        )
        canciones.add(
            Song(R.raw.edsheeranlegohouse, R.drawable.legohouse, "Lego house")
        )
        canciones.add(
            Song(R.raw.edsheeranone, R.drawable.one, "One")
        )
        canciones.add(
            Song(R.raw.edsheeranperfect, R.drawable.perfect, "Perfect")
        )
        canciones.add(
            Song(R.raw.edsheerantheateam, R.drawable.theateam, "The a team")
        )
        canciones.add(
            Song(R.raw.edsheeranthinkingoutloud, R.drawable.thinkingoutloud, "Thinking out loud")
        )
        canciones.add(
            Song(R.raw.happieredsheeranlyric, R.drawable.happier, "Happier")
        )
        canciones.add(
            Song(R.raw.photographedsheeran, R.drawable.photograph, "Photograph")
        )
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
        viewAdapter = SongAdapter(activity!!, canciones) { itemClick(it) }

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

    private fun itemClick(song: Song){
        Toast.makeText(context, "presiono la canción ${song.name}", Toast.LENGTH_SHORT).show()
    }
}