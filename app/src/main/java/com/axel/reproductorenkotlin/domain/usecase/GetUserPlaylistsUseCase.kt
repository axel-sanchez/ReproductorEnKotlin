package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.UserPlaylists
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

interface GetUserPlaylistsUseCase{
    suspend fun call(): List<UserPlaylists.Item?>
}

/**
 * Caso de uso para
 * @author Axel Sanchez
 */
class GetUserPlaylistsUseCaseImpl : GetUserPlaylistsUseCase, KoinComponent {
    private val api: ConnectToApi by inject()

    override suspend fun call(): List<UserPlaylists.Item?> {
        return api.getUserPlaylists().value?.items?: listOf()
    }
}