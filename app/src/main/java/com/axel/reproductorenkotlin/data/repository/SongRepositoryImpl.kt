package com.axel.reproductorenkotlin.data.repository

import androidx.lifecycle.MutableLiveData
import com.axel.reproductorenkotlin.data.models.FeaturedPlaylistSong
import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.data.models.Search
import com.axel.reproductorenkotlin.data.source.SongRemoteSource
import com.axel.reproductorenkotlin.domain.repository.SongRepository

/**
 * @author Axel Sanchez
 */
class SongRepositoryImpl(private val songRemoteSource: SongRemoteSource): SongRepository {
    override suspend fun getFeaturedPlaylistSong(): MutableLiveData<MutableList<FeaturedPlaylistSong?>> {
        return songRemoteSource.getFeaturedPlaylistSong()
    }

    override suspend fun getPlaylistSongs(idPlaylist: String): MutableLiveData<PlaylistSongs?> {
        return songRemoteSource.getPlaylistSongs(idPlaylist)
    }

    override suspend fun getSongsBySearch(query: String): MutableLiveData<Search?> {
        return songRemoteSource.getSongsBySearch(query)
    }
}