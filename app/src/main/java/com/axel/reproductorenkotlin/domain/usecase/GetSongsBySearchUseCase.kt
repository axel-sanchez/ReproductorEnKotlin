package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.Search
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

interface GetSongsBySearchUseCase{
    suspend fun call(query: String): List<Search.Tracks.Item?>
}

/**
 * @author Axel Sanchez
 */
class GetSongsBySearchUseCaseImpl: GetSongsBySearchUseCase, KoinComponent {
    private val api: ConnectToApi by inject()

    override suspend fun call(query: String): List<Search.Tracks.Item?> {
        return api.getSongsBySearch(query).value?.tracks?.items ?: listOf()
    }
}