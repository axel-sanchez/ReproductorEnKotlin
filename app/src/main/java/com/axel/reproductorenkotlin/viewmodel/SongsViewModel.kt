package com.axel.reproductorenkotlin.viewmodel

import androidx.lifecycle.*
import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.data.models.Search
import com.axel.reproductorenkotlin.domain.SongsUseCase
import kotlinx.coroutines.launch

/**
 * View model de [SongsFragment]
 * @author Axel Sanchez
 */
class SongsViewModel(private val songsUseCase: SongsUseCase) : ViewModel() {

    private val listDataPlaylistSongs = MutableLiveData<List<PlaylistSongs.Item.Track?>>()
    private val listDataSongs = MutableLiveData<List<Search.Tracks.Item?>>()

    private fun setListDataPlaylistSongs(songs: List<PlaylistSongs.Item.Track?>) {
        listDataPlaylistSongs.postValue(songs)
    }

    private fun setListDataSongs(songs: List<Search.Tracks.Item?>) {
        listDataSongs.postValue(songs)
    }

    fun getPlaylistSongs(idPlaylist: String) {
        viewModelScope.launch {
            setListDataPlaylistSongs(songsUseCase.getPlaylistSongs(idPlaylist))
        }
    }

    fun getSongsByQuery(query: String) {
        viewModelScope.launch {
            setListDataSongs(songsUseCase.getSongsBySearch(query))
        }
    }

    fun getPlaylistSongsLiveData(): LiveData<List<PlaylistSongs.Item.Track?>> {
        return listDataPlaylistSongs
    }

    fun getSongsByQueryLiveData(): LiveData<List<Search.Tracks.Item?>> {
        return listDataSongs
    }

    class SongsViewModelFactory(private val songsUseCase: SongsUseCase) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(SongsUseCase::class.java).newInstance(songsUseCase)
        }
    }
}
