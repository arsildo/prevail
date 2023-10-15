package com.arsildo.prevail

import android.app.Application
import com.arsildo.core.preferences.di.datastoreModule
import com.arsildo.network.di.networkModule
import com.arsildo.prevail.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class PrevailApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@PrevailApplication)
            modules(appModule)
        }
    }
}