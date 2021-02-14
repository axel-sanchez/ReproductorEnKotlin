package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.domain.repository.SongRepository

interface GetPlaylistSongsUseCase {
    suspend fun call(idPlaylist: String): List<PlaylistSongs.Item.Track?>
}

/**
 * @author Axel Sanchez
 */
class GetPlaylistSongsUseCaseImpl(private val repository: SongRepository): GetPlaylistSongsUseCase {

    override suspend fun call(idPlaylist: String): List<PlaylistSongs.Item.Track?> {
        return repository.getPlaylistSongs(idPlaylist).value?.items?.map { it?.track }?: listOf()
    }
}