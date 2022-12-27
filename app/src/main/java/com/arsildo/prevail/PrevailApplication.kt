package com.arsildo.prevail

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class PrevailApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // ADD TIMBER
    }
}