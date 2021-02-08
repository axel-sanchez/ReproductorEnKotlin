package com.axel.reproductorenkotlin.viewmodel

import androidx.lifecycle.*
import com.axel.reproductorenkotlin.data.models.ItemSong
import com.axel.reproductorenkotlin.domain.usecase.GetItemSongListUseCase
import kotlinx.coroutines.launch

/**
 * View model de [ExploreFragment]
 * @author Axel Sanchez
 */
class ExploreViewModel(private val getItemSongListUseCase: GetItemSongListUseCase) : ViewModel() {

    private val listData: MutableLiveData<MutableList<ItemSong?>> by lazy {
        MutableLiveData<MutableList<ItemSong?>>().also {
            getItemSongList()
        }
    }

    private fun setListData(itemSongList: MutableList<ItemSong?>) {
        listData.postValue(itemSongList)
    }

    private fun getItemSongList() {
        viewModelScope.launch {
            setListData(getItemSongListUseCase.call())
        }
    }

    fun getItemSongListLiveData(): LiveData<MutableList<ItemSong?>> {
        return listData
    }

    class ExploreViewModelFactory(private val getItemSongListUseCase: GetItemSongListUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(GetItemSongListUseCase::class.java).newInstance(getItemSongListUseCase)
        }
    }
}