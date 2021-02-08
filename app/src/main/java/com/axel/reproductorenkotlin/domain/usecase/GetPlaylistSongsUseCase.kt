package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

interface GetPlaylistSongsUseCase {
    suspend fun call(idPlaylist: String): List<PlaylistSongs.Item.Track?>
}

/**
 * @author Axel Sanchez
 */
class GetPlaylistSongsUseCaseImpl: GetPlaylistSongsUseCase, KoinComponent {
    private val api: ConnectToApi by inject()

    override suspend fun call(idPlaylist: String): List<PlaylistSongs.Item.Track?> {
        return api.getPlaylistSongs(idPlaylist).value?.items?.map { it?.track }?: listOf()
    }
}