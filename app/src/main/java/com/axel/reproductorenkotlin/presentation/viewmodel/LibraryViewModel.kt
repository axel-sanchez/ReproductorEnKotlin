package com.axel.reproductorenkotlin.presentation.viewmodel

import androidx.lifecycle.*
import com.axel.reproductorenkotlin.data.models.UserPlaylists
import com.axel.reproductorenkotlin.domain.usecase.GetUserPlaylistsUseCase
import kotlinx.coroutines.launch

/**
 * View model de [LibraryFragment]
 * @author Axel Sanchez
 */
class LibraryViewModel(private val getUserPlaylistsUseCase: GetUserPlaylistsUseCase) : ViewModel() {

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
            setListData(getUserPlaylistsUseCase.call())
        }
    }

    fun getUserPlaylistsLiveData(): LiveData<List<UserPlaylists.Item?>> {
        return listData
    }

    class LibraryViewModelFactory(private val getUserPlaylistsUseCase: GetUserPlaylistsUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(GetUserPlaylistsUseCase::class.java).newInstance(getUserPlaylistsUseCase)
        }
    }
}