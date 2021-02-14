package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.data.service.ConnectToApi

interface GetPlaylistSongsUseCase {
    suspend fun call(idPlaylist: String): List<PlaylistSongs.Item.Track?>
}

/**
 * @author Axel Sanchez
 */
class GetPlaylistSongsUseCaseImpl(private val api: ConnectToApi): GetPlaylistSongsUseCase {

    override suspend fun call(idPlaylist: String): List<PlaylistSongs.Item.Track?> {
        return api.getPlaylistSongs(idPlaylist).value?.items?.map { it?.track }?: listOf()
    }
}