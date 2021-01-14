package com.axel.reproductorenkotlin.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.data.models.FeaturedPlaylist
import com.axel.reproductorenkotlin.data.models.ItemSong
import com.axel.reproductorenkotlin.data.models.Token
import com.axel.reproductorenkotlin.data.service.ApiService
import com.axel.reproductorenkotlin.databinding.FragmentHomeBinding
import com.axel.reproductorenkotlin.ui.view.adapter.ExploreAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.spotify.com/v1/"

class ExploreFragment: Fragment() {

    private lateinit var serviceToken: ApiService
    private lateinit var service: ApiService

    private var listItems = mutableListOf<ItemSong>()

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    var bearer = ""

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

    private fun setAdapter() {
        viewAdapter = ExploreAdapter(listItems) { itemClick(it) }

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

    private fun itemClick(playlist: ItemSong){
        Toast.makeText(context, "Presionó la lista ${playlist.getName()}", Toast.LENGTH_SHORT).show()
    }
}