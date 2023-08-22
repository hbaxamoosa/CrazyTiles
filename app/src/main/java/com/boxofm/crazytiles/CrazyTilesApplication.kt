package com.boxofm.crazytiles

import android.app.Application
import com.google.firebase.FirebaseApp
import timber.log.Timber

class CrazyTilesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

