package com.axel.reproductorenkotlin.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

/**
 * Helper para realizar acciones relacionadas a la conexión a internet
 * @author Axel Sanchez
 * */
object NetworkHelper {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = Objects.requireNonNull(cm).activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}