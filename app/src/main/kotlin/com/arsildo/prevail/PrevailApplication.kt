package com.arsildo.prevail

import android.app.Application
import com.arsildo.core.preferences.di.datastoreModule
import com.arsildo.network.di.networkModule
import com.arsildo.prevail.di.appModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PrevailApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(level = if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            androidContext(this@PrevailApplication)
            modules(
                networkModule,
                datastoreModule,
                appModule
            )
        }
    }
}