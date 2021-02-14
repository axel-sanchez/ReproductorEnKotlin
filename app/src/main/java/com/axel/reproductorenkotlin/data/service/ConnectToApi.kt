package com.axel.reproductorenkotlin.data.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.axel.reproductorenkotlin.data.models.ItemSong
import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.data.models.Search

/**
 * @author Axel Sanchez
 */
class ConnectToApi(private val service: ApiService) {
    suspend fun getItemSongList(): MutableLiveData<MutableList<ItemSong?>> {
        var mutableLiveData = MutableLiveData<MutableList<ItemSong?>>()

        try {
            val response = service.getFeaturedPlaylists("Bearer $token", "AR")
            if (response.isSuccessful) {
                Log.i("Successful Response", response.body().toString())
                mutableLiveData.value =
                    response.body()?.getPlaylists()?.getItems()?.toMutableList() ?: mutableListOf()
            } else {
                Log.i("Error Response", response.errorBody().toString())
                mutableLiveData.value = mutableListOf()
            }
        } catch (e: Exception) {
            mutableLiveData.value = mutableListOf()
            Log.e("ConnectToApi", "Error al obtener las playlists y guardarlos en el livedata")
            e.printStackTrace()
        }

        return mutableLiveData
    }

    suspend fun getPlaylistSongs(idPlaylist: String): MutableLiveData<PlaylistSongs?> {
        var mutableLiveData = MutableLiveData<PlaylistSongs?>()

        try {
            val response = service.getSongsFromPlaylist("Bearer $token", idPlaylist, "es")
            if (response.isSuccessful) {
                Log.i("Successful Response", response.body().toString())
                mutableLiveData.value = response.body()
            } else {
                Log.i("Error Response", response.errorBody().toString())
                mutableLiveData.value = null
            }
        } catch (e: Exception) {
            mutableLiveData.value = null
            Log.e("ConnectToApi", "Error al obtener las playlists del usuario y guardarlos en el livedata")
            e.printStackTrace()
        }

        return mutableLiveData
    }

    suspend fun getSongsBySearch(query: String): MutableLiveData<Search?> {
        var mutableLiveData = MutableLiveData<Search?>()

        try {
            val response = service.getSongsBySearch(token = "Bearer $token", query = query)
            if (response.isSuccessful) {
                Log.i("Successful Response", response.body().toString())
                mutableLiveData.value = response.body()
            } else {
                Log.i("Error Response", response.errorBody().toString())
                mutableLiveData.value = null
            }
        } catch (e: Exception) {
            mutableLiveData.value = null
            Log.e("ConnectToApi", "Error al obtener las canciones de la búsqueda y guardarlas en el livedata")
            e.printStackTrace()
        }

        return mutableLiveData
    }

    companion object {
        var token = ""
    }
}