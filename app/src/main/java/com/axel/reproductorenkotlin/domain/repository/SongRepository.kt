package com.axel.reproductorenkotlin.domain.repository

import androidx.lifecycle.MutableLiveData
import com.axel.reproductorenkotlin.data.models.FeaturedPlaylistSong
import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.data.models.Search

/**
 * @author Axel Sanchez
 */
interface SongRepository {
    suspend fun getFeaturedPlaylistSong(): MutableLiveData<MutableList<FeaturedPlaylistSong?>>
    suspend fun getPlaylistSongs(idPlaylist: String): MutableLiveData<PlaylistSongs?>
    suspend fun getSongsBySearch(query: String): MutableLiveData<Search?>
}