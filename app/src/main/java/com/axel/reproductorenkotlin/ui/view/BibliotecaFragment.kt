package com.axel.reproductorenkotlin.ui.view

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.Cancion
import com.axel.reproductorenkotlin.ui.view.adapter.CancionAdapter
import com.axel.reproductorenkotlin.ui.view.customs.ReproductorFragment
import com.axel.reproductorenkotlin.ui.view.interfaces.IOnBackPressFragment
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File

class BibliotecaFragment : ReproductorFragment() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var canciones: MutableList<Cancion>

    override fun OnBackPressFragment() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        canciones = mutableListOf()

        //TODO: OBTENER LAS CANCIONES DE LA MEMORIA DEL CELULAR
        getSongsMemory()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setAdapter()
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

    private fun itemClick(cancion: Cancion) {
        println("presiono la canción ${cancion.getNombre()}")
    }


    private fun getSongsMemory() {

        //readSongs(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))

        val resolver: ContentResolver = activity!!.contentResolver
        val uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
        //val uri = Uri.parse("/storage/emulated/0/Download")
        val cursor: Cursor? = resolver.query(uri, null, null, null, null)

        var nombres = mutableListOf<String>()

        when {
            cursor == null -> {
                // query failed, handle error.
                Toast.makeText(context, "Cursor nulo", Toast.LENGTH_SHORT).show()
            }
            !cursor.moveToFirst() -> {
                // no media on the device
                Toast.makeText(context, "Cursor vacio", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val titleColumn: Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val idColumn: Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                do {
                    val thisId = cursor.getLong(idColumn)
                    val thisTitle = cursor.getString(titleColumn)
                    nombres.add(thisTitle)
                    // ...process entry...
                } while (cursor.moveToNext())
                println(nombres)
            }
        }
        cursor?.close()
    }

    private fun readSongs(root: File): MutableList<File>{
        var mutableList = mutableListOf<File>()
        var files = root.listFiles()

        for(file in files){
            if(file.isDirectory){
                mutableList.addAll(readSongs(file))
            } else{
                if(file.name.endsWith(".mp3")){
                    mutableList.add(file)
                }
            }
        }

        return mutableList
    }
}