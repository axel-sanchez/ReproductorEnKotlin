package com.axel.reproductorenkotlin.viewmodel

import androidx.lifecycle.*
import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.domain.SongsUseCase
import kotlinx.coroutines.launch

/**
 * View model de [SongsFragment]
 * @author Axel Sanchez
 */
class SongsViewModel(private val songsUseCase: SongsUseCase) : ViewModel() {

    private val listData = MutableLiveData<List<PlaylistSongs.Item.Track?>>()

    private fun setListData(songs: List<PlaylistSongs.Item.Track?>) {
        listData.postValue(songs)
    }

    fun getPlaylistSongs(idPlaylist: String) {
        viewModelScope.launch {
            setListData(songsUseCase.getPlaylistSongs(idPlaylist))
        }
    }

    fun getPlaylistSongsLiveData(): LiveData<List<PlaylistSongs.Item.Track?>> {
        return listData
    }

    class SongsViewModelFactory(private val songsUseCase: SongsUseCase) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(SongsUseCase::class.java).newInstance(songsUseCase)
        }
    }
}
