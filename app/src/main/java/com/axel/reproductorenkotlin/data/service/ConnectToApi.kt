package com.axel.reproductorenkotlin.data.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.axel.reproductorenkotlin.data.models.ItemSong
import com.axel.reproductorenkotlin.data.models.User
import com.axel.reproductorenkotlin.data.models.UserPlaylists
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

    suspend fun getUser(): MutableLiveData<User?> {
        var mutableLiveData = MutableLiveData<User?>()

        try {
            val response = service.getUser("Bearer $token")
            if (response.isSuccessful) {
                Log.i("Successful Response", response.body().toString())
                mutableLiveData.value = response.body()
            } else {
                Log.i("Error Response", response.errorBody().toString())
                mutableLiveData.value = null
            }
        } catch (e: Exception) {
            mutableLiveData.value = null
            Log.e("ConnectToApi", "Error al obtener el usuario y guardarlos en el livedata")
            e.printStackTrace()
        }

        return mutableLiveData
    }

    suspend fun getUserPlaylists(): MutableLiveData<UserPlaylists?> {
        var mutableLiveData = MutableLiveData<UserPlaylists?>()

        try {
            val response = service.getUserPlaylists("Bearer $token")
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

    companion object {
        var token = ""
    }
}