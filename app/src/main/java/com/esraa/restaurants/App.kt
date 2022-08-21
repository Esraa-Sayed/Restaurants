package com.esraa.restaurants

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        //setup timber for debug
        if(BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}