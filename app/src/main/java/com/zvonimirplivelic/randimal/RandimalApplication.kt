package com.zvonimirplivelic.randimal

import android.app.Application
import timber.log.Timber

class RandimalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}