package com.axel.reproductorenkotlin.domain.repository

import androidx.lifecycle.MutableLiveData
import com.axel.reproductorenkotlin.data.models.User
import com.axel.reproductorenkotlin.data.models.UserPlaylists

/**
 * @author Axel Sanchez
 */
interface UserRepository {
    suspend fun getUser(): MutableLiveData<User?>
    suspend fun getUserPlaylists(): MutableLiveData<UserPlaylists?>
}