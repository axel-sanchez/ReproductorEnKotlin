package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.FeaturedPlaylistSong
import com.axel.reproductorenkotlin.data.service.ConnectToApi

interface GetFeaturedPlaylistSongsUseCase{
    suspend fun call(): MutableList<FeaturedPlaylistSong?>
}

/**
 * Caso de uso para
 * @author Axel Sanchez
 */
class GetFeaturedPlaylistSongsUseCaseImpl(private val api: ConnectToApi): GetFeaturedPlaylistSongsUseCase {
    override suspend fun call(): MutableList<FeaturedPlaylistSong?> {
        return api.getItemSongList().value?: mutableListOf()
    }
}