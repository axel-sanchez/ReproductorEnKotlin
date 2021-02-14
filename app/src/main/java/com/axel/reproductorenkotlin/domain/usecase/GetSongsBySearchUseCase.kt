package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.Search
import com.axel.reproductorenkotlin.domain.repository.SongRepository

interface GetSongsBySearchUseCase{
    suspend fun call(query: String): List<Search.Tracks.Item?>
}

/**
 * @author Axel Sanchez
 */
class GetSongsBySearchUseCaseImpl(private val repository: SongRepository): GetSongsBySearchUseCase {
    override suspend fun call(query: String): List<Search.Tracks.Item?> {
        return repository.getSongsBySearch(query).value?.tracks?.items ?: listOf()
    }
}