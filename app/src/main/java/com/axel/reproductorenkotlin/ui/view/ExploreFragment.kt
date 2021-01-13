package com.axel.reproductorenkotlin.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.Song
import com.axel.reproductorenkotlin.data.models.FeaturedPlaylist
import com.axel.reproductorenkotlin.data.models.ItemSong
import com.axel.reproductorenkotlin.data.models.Token
import com.axel.reproductorenkotlin.ui.view.adapter.PlaylistAdapter
import com.axel.reproductorenkotlin.ui.view.customs.ReproductorFragment
import com.axel.reproductorenkotlin.ui.view.interfaces.ApiService
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

const val BASE_URL = "https://api.spotify.com/v1/"

class ExploreFragment: ReproductorFragment() {

    private lateinit var canciones: MutableList<Song>

    private lateinit var serviceToken: ApiService
    private lateinit var service: ApiService

    private var listItems = mutableListOf<ItemSong>()

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    var bearer = ""

    override fun onBackPressFragment() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val retrofitToken: Retrofit = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        serviceToken = retrofitToken.create(ApiService::class.java)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ApiService::class.java)

        getToken()

    }

    private fun getToken() {
        //Recibimos el token de spotify
        serviceToken.getToken().enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.isSuccessful) {
                    println("body token: ${response.body()!!.getAccessToken()}")
                    bearer = response.body()!!.getAccessToken()
                    //search()

                    getFeaturedPlaylists()
                } else {
                    println("response token: $response")
                }
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getFeaturedPlaylists() {
        try {
            service.getFeaturedPlaylists(
                "Bearer $bearer"
            ).enqueue(object : Callback<FeaturedPlaylist> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<FeaturedPlaylist>, response: Response<FeaturedPlaylist>) {
                    if (response.isSuccessful) {
                        var body = response.body()

                        listItems = body!!.getPlaylists().getItems().toMutableList()

                        listItems.removeIf { x -> x.getImages()[0].getUrl().isNullOrEmpty() }

                        setAdapter()
                    } else {
                        println("error response song: $response")
                    }
                }

                override fun onFailure(call: Call<FeaturedPlaylist>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setAdapter() {
        viewAdapter = PlaylistAdapter(listItems) { itemClick(it) }

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

    private fun itemClick(playlist: ItemSong){
        Toast.makeText(context, "Presionó la lista ${playlist.getName()}", Toast.LENGTH_SHORT).show()
    }
}