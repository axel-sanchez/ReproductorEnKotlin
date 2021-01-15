package com.axel.reproductorenkotlin.data.models

/**
 * @author Axel Sanchez
 */
data class MyTime(
    var minuteMilliSeconds: Float = 0.0f,
    var durationMilliSeconds: Float = 0.0f,
    var iteratorSeconds: Float = 0.0f,
    var requireNextSong: Boolean = false
)