package com.axel.reproductorenkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axel.reproductorenkotlin.data.models.MyTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * View model de [PlayerFragment]
 * @author Axel Sanchez
 */
class PlayerViewModel : ViewModel() {

    var canExecute = true

    var time = MyTime()

    private lateinit var job: Job

    private val listData = MutableLiveData<MyTime>()

    private fun setListData(myTime: MyTime) {
        listData.postValue(myTime)
    }

    fun getTime() {
        job = viewModelScope.launch {
            countTimer()
        }
    }

    fun getTimeLiveData(): LiveData<MyTime> {
        return listData
    }

    private suspend fun countTimer() {
        while (canExecute) {

            time.requireNextSong = time.iteratorSeconds >= (time.durationMilliSeconds / 1000f)

            if(time.requireNextSong){
                setListData(time)
                delay(1000)
            }
            else{
                time.minuteMilliSeconds = time.iteratorSeconds * 1000
                time.iteratorSeconds++
                setListData(time)
                delay(1000)
            }
        }
        job.cancel()
    }
}