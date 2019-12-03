package com.boxofm.crazytiles

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber

class CrazyTilesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Required initialization logic here!

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Stetho.initializeWithDefaults(this)
    }
}

