package com.axel.reproductorenkotlin.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.axel.reproductorenkotlin.data.models.User
import com.axel.reproductorenkotlin.data.models.UserPlaylists
import com.axel.reproductorenkotlin.data.service.ApiService
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

interface UserRemoteSource{
    suspend fun getUser(): MutableLiveData<User?>
    suspend fun getUserPlaylists(): MutableLiveData<UserPlaylists?>
}

/**
 * @author Axel Sanchez
 */
class UserRemoteSourceImpl(private val service: ApiService): UserRemoteSource, KoinComponent {

    override suspend fun getUser(): MutableLiveData<User?> {
        var mutableLiveData = MutableLiveData<User?>()

        try {
            val response = service.getUser("Bearer ${ConnectToApi.token}")
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

    override suspend fun getUserPlaylists(): MutableLiveData<UserPlaylists?> {
        var mutableLiveData = MutableLiveData<UserPlaylists?>()

        try {
            val response = service.getUserPlaylists("Bearer ${ConnectToApi.token}")
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
}