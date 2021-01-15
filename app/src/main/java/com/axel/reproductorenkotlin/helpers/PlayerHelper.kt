package com.axel.reproductorenkotlin.helpers

object PlayerHelper {
    fun millisToMinutesAndSeconds(millis: Float): String {
        val minutes: Float = (millis / 1000f / 60f)
        val seconds: Float = (minutes - minutes.toInt()) * 60f
        val secondString: String
        val minuteString: String

        secondString = if (seconds.toInt() < 10) {
            "0${seconds.toInt()}"
        } else {
            "${seconds.toInt()}"
        }

        minuteString = if (minutes.toInt() < 10) {
            "0${minutes.toInt()}"
        } else {
            "${minutes.toInt()}"
        }

        return "$minuteString:$secondString"
    }
}