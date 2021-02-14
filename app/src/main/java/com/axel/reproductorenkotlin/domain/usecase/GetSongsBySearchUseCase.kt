package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.Search
import com.axel.reproductorenkotlin.data.service.ConnectToApi

interface GetSongsBySearchUseCase{
    suspend fun call(query: String): List<Search.Tracks.Item?>
}

/**
 * @author Axel Sanchez
 */
class GetSongsBySearchUseCaseImpl(private val api: ConnectToApi): GetSongsBySearchUseCase {
    override suspend fun call(query: String): List<Search.Tracks.Item?> {
        return api.getSongsBySearch(query).value?.tracks?.items ?: listOf()
    }
}