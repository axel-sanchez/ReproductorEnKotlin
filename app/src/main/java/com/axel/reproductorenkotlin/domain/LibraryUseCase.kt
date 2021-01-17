package com.axel.reproductorenkotlin.domain

import com.axel.reproductorenkotlin.data.models.UserPlaylists
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Caso de uso para
 * @author Axel Sanchez
 */
class LibraryUseCase : KoinComponent {
    private val api: ConnectToApi by inject()

    suspend fun getUserPlaylistsOnline(): List<UserPlaylists.Item?> {
        return api.getUserPlaylists().value?.items?: listOf()
    }
}