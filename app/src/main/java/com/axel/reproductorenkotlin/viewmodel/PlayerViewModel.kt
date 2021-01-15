package com.axel.reproductorenkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axel.reproductorenkotlin.data.models.MyTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * View model de [PlayerFragment]
 * @author Axel Sanchez
 */
class PlayerViewModel : ViewModel() {

    var canExecute = true

    var time = MyTime()

    private val listData = MutableLiveData<MyTime>()

    private fun setListData(myTime: MyTime) {
        listData.postValue(myTime)
    }

    fun getTime() {
        viewModelScope.launch {
            countTimer()
        }
    }

    fun getTimeLiveData(): LiveData<MyTime> {
        return listData
    }

    private suspend fun countTimer() {
        while (time.iteratorSeconds < (time.durationMilliSeconds / 1000f) && canExecute) {
            time.minuteMilliSeconds = time.iteratorSeconds * 1000 //En milisegundos
            time.iteratorSeconds++
            setListData(time)
            delay(1000)
        }
        if (time.iteratorSeconds >= (time.durationMilliSeconds / 1000f)) {
            time.requireNextSong = true
            setListData(time)
        } else{
            time.requireNextSong = false
            setListData(time)
        }
    }
}