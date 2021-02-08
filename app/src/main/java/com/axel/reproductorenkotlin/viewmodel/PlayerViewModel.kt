package com.axel.reproductorenkotlin.viewmodel

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.*
import com.axel.reproductorenkotlin.data.models.MyTime
import com.axel.reproductorenkotlin.helpers.SongHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

/**
 * View model de [PlayerFragment]
 * @author Axel Sanchez
 */
class PlayerViewModel(private val context: WeakReference<Context>) : ViewModel() {

    var job: Job? = null

    private var canExecute = true
    fun setCanExecute(newCanExecute: Boolean){
        canExecute = newCanExecute
    }
    fun setNextPosition(){
        position++
    }
    fun setPreviousPosition(){
        position--
    }

    private var position: Int = 0
    fun getPosition() = position
    fun setPosition(newPosition: Int) {
        position = newPosition
    }

    private var time = MyTime()
    fun getTime() = time

    private lateinit var mediaPlayer: MediaPlayer

    init {
        initializeMediaPlayer()
    }

    private val listData = MutableLiveData<MyTime>()

    private fun setListData(myTime: MyTime) {
        listData.postValue(myTime)
    }

    fun runSong() {
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

            if (time.requireNextSong) {
                setListData(time)
                delay(1000)
            } else {
                time.minuteMilliSeconds = time.iteratorSeconds * 1000
                time.iteratorSeconds++
                setListData(time)
                delay(1000)
            }
        }
        job?.cancel()
    }

    fun playSong() {
        mediaPlayer.start()
    }

    fun pauseSong() {
        mediaPlayer.pause()
    }

    fun initializeMediaPlayer() {
        mediaPlayer = MediaPlayer.create(context.get(), SongHelper.songsList[position].song)
    }

    fun mediaPlayerIsPlaying() = mediaPlayer.isPlaying

    fun getSongDuration() = mediaPlayer.duration.toFloat()

    fun resetAndPrepareMediaPlayer() {
        try {
            mediaPlayer.reset()
            mediaPlayer.prepareAsync()
            mediaPlayer.stop()
            mediaPlayer.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setMediaPlayerLooping(isLooping: Boolean) {
        mediaPlayer.isLooping = isLooping
    }

    fun setNewProgress(progress: Int) {
        mediaPlayer.seekTo(progress)
    }

    class PlayerViewModelFactory(private val context: WeakReference<Context>) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(WeakReference::class.java).newInstance(context)
        }
    }
}