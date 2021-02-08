package com.axel.reproductorenkotlin.viewmodel

import androidx.lifecycle.*
import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.data.models.Search
import com.axel.reproductorenkotlin.domain.usecase.GetPlaylistSongsUseCase
import com.axel.reproductorenkotlin.domain.usecase.GetSongsBySearchUseCase
import kotlinx.coroutines.launch

/**
 * View model de [SongsFragment]
 * @author Axel Sanchez
 */
class SongsViewModel(private val getPlaylistSongsUseCase: GetPlaylistSongsUseCase, private val getSongsBySearchUseCase: GetSongsBySearchUseCase) : ViewModel() {

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
            setListDataPlaylistSongs(getPlaylistSongsUseCase.call(idPlaylist))
        }
    }

    fun getSongsByQuery(query: String) {
        viewModelScope.launch {
            setListDataSongs(getSongsBySearchUseCase.call(query))
        }
    }

    fun getPlaylistSongsLiveData(): LiveData<List<PlaylistSongs.Item.Track?>> {
        return listDataPlaylistSongs
    }

    fun getSongsByQueryLiveData(): LiveData<List<Search.Tracks.Item?>> {
        return listDataSongs
    }

    class SongsViewModelFactory(private val getPlaylistSongsUseCase: GetPlaylistSongsUseCase, private val getSongsBySearchUseCase: GetSongsBySearchUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(GetPlaylistSongsUseCase::class.java, GetSongsBySearchUseCase::class.java).newInstance(getPlaylistSongsUseCase, getSongsBySearchUseCase)
        }
    }
}
