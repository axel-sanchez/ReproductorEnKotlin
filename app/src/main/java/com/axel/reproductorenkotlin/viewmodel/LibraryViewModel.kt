package com.axel.reproductorenkotlin.viewmodel

import androidx.lifecycle.*
import com.axel.reproductorenkotlin.data.models.UserPlaylists
import com.axel.reproductorenkotlin.domain.LibraryUseCase
import kotlinx.coroutines.launch

/**
 * View model de [LibraryFragment]
 * @author Axel Sanchez
 */
class LibraryViewModel(private val libraryUseCase: LibraryUseCase) : ViewModel() {

    private val listData: MutableLiveData<List<UserPlaylists.Item?>> by lazy {
        MutableLiveData<List<UserPlaylists.Item?>>().also {
            getUserPlaylists()
        }
    }

    private fun setListData(userPlaylists: List<UserPlaylists.Item?>) {
        listData.postValue(userPlaylists)
    }

    private fun getUserPlaylists() {
        viewModelScope.launch {
            setListData(libraryUseCase.getUserPlaylistsOnline())
        }
    }

    fun getUserPlaylistsLiveData(): LiveData<List<UserPlaylists.Item?>> {
        return listData
    }

    class LibraryViewModelFactory(private val libraryUseCase: LibraryUseCase) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(LibraryUseCase::class.java).newInstance(libraryUseCase)
        }
    }
}