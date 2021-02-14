package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.UserPlaylists
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import com.axel.reproductorenkotlin.data.source.UserRemoteSource
import com.axel.reproductorenkotlin.domain.repository.UserRepository
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

interface GetUserPlaylistsUseCase{
    suspend fun call(): List<UserPlaylists.Item?>
}

/**
 * Caso de uso para
 * @author Axel Sanchez
 */
class GetUserPlaylistsUseCaseImpl(private val repository: UserRepository) : GetUserPlaylistsUseCase, KoinComponent {
    override suspend fun call(): List<UserPlaylists.Item?> {
        return repository.getUserPlaylists().value?.items?: listOf()
    }
}