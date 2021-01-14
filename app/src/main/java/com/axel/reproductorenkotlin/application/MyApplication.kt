package com.axel.reproductorenkotlin.application

import android.app.Application
import com.axel.reproductorenkotlin.di.moduleApp
import org.koin.android.ext.android.startKoin

/**
 * @author Axel Sanchez
 */
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(applicationContext, listOf(moduleApp))
    }
}