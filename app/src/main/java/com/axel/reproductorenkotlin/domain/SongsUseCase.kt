package com.axel.reproductorenkotlin.domain

import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.data.models.Search
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Caso de uso para
 * @author Axel Sanchez
 */
class SongsUseCase : KoinComponent {
    private val api: ConnectToApi by inject()

    suspend fun getPlaylistSongs(idPlaylist: String): List<PlaylistSongs.Item.Track?> {
        return api.getPlaylistSongs(idPlaylist).value?.items?.map { it?.track }?: listOf()
    }

    suspend fun getSongsBySearch(query: String): List<Search.Tracks.Item?> {
        return api.getSongsBySearch(query).value?.tracks?.items?: listOf()
    }
}