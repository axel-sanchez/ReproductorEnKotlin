package com.axel.reproductorenkotlin.data.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.axel.reproductorenkotlin.data.models.ItemSong
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * @author Axel Sanchez
 */
class ConnectToApi : KoinComponent {

    private val serviceToken: ApiService by inject(name = "serviceToken")
    private val service: ApiService by inject(name = "service")

    private suspend fun getToken(): MutableLiveData<String?> {
        var mutableLiveData = MutableLiveData<String?>()

        try {
            val response = serviceToken.getToken()
            if (response.isSuccessful) {
                Log.i("Successful Token", response.body()?.getAccessToken() ?: "")
                mutableLiveData.value = response.body()?.getAccessToken()
            } else {
                Log.i("Error Response", response.errorBody().toString())
                mutableLiveData.value = null
            }
        } catch (e: Exception) {
            mutableLiveData.value = null
            Log.e("ConnectToApi", "Error al obtener el token y guardarlos en el livedata")
            e.printStackTrace()
        }

        return mutableLiveData
    }

    suspend fun getItemSongList(): MutableLiveData<MutableList<ItemSong?>> {
        var mutableLiveData = MutableLiveData<MutableList<ItemSong?>>()
        val token = getToken().value

        token?.let {
            try {
                val response = service.getFeaturedPlaylists("Bearer $token")
                if (response.isSuccessful) {
                    Log.i("Successful Response", response.body().toString())
                    mutableLiveData.value = response.body()?.getPlaylists()?.getItems()?.toMutableList() ?: mutableListOf()
                } else {
                    Log.i("Error Response", response.errorBody().toString())
                    mutableLiveData.value = mutableListOf()
                }
            } catch (e: Exception) {
                mutableLiveData.value = mutableListOf()
                Log.e("ConnectToApi", "Error al obtener las playlists y guardarlos en el livedata")
                e.printStackTrace()
            }
        } ?: kotlin.run { mutableLiveData.value = mutableListOf() }

        return mutableLiveData
    }
}