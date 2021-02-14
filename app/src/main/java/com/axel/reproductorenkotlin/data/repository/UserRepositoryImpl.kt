package com.axel.reproductorenkotlin.data.repository

import androidx.lifecycle.MutableLiveData
import com.axel.reproductorenkotlin.data.models.User
import com.axel.reproductorenkotlin.data.models.UserPlaylists
import com.axel.reproductorenkotlin.data.source.UserRemoteSource
import com.axel.reproductorenkotlin.domain.repository.UserRepository

/**
 * @author Axel Sanchez
 */
class UserRepositoryImpl(private val userRemoteSource: UserRemoteSource): UserRepository {
    override suspend fun getUser(): MutableLiveData<User?> {
        return userRemoteSource.getUser()
    }

    override suspend fun getUserPlaylists(): MutableLiveData<UserPlaylists?>{
        return userRemoteSource.getUserPlaylists()
    }
}