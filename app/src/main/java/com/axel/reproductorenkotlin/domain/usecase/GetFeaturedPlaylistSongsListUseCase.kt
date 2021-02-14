package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.FeaturedPlaylistSong
import com.axel.reproductorenkotlin.domain.repository.SongRepository

interface GetFeaturedPlaylistSongsUseCase{
    suspend fun call(): MutableList<FeaturedPlaylistSong?>
}

/**
 * Caso de uso para
 * @author Axel Sanchez
 */
class GetFeaturedPlaylistSongsUseCaseImpl(private val repository: SongRepository): GetFeaturedPlaylistSongsUseCase {
    override suspend fun call(): MutableList<FeaturedPlaylistSong?> {
        return repository.getFeaturedPlaylistSong().value?: mutableListOf()
    }
}