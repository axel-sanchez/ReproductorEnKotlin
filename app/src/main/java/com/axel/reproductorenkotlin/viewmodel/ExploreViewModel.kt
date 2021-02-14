package com.axel.reproductorenkotlin.viewmodel

import androidx.lifecycle.*
import com.axel.reproductorenkotlin.data.models.FeaturedPlaylistSong
import com.axel.reproductorenkotlin.domain.usecase.GetFeaturedPlaylistSongsUseCase
import kotlinx.coroutines.launch

/**
 * View model de [ExploreFragment]
 * @author Axel Sanchez
 */
class ExploreViewModel(private val getFeaturedPlaylistSongsUseCase: GetFeaturedPlaylistSongsUseCase) : ViewModel() {

    private val listData: MutableLiveData<MutableList<FeaturedPlaylistSong?>> by lazy {
        MutableLiveData<MutableList<FeaturedPlaylistSong?>>().also {
            getItemSongList()
        }
    }

    private fun setListData(featuredPlaylistSongList: MutableList<FeaturedPlaylistSong?>) {
        listData.postValue(featuredPlaylistSongList)
    }

    private fun getItemSongList() {
        viewModelScope.launch {
            setListData(getFeaturedPlaylistSongsUseCase.call())
        }
    }

    fun getItemSongListLiveData(): LiveData<MutableList<FeaturedPlaylistSong?>> {
        return listData
    }

    class ExploreViewModelFactory(private val getFeaturedPlaylistSongsUseCase: GetFeaturedPlaylistSongsUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(GetFeaturedPlaylistSongsUseCase::class.java).newInstance(getFeaturedPlaylistSongsUseCase)
        }
    }
}