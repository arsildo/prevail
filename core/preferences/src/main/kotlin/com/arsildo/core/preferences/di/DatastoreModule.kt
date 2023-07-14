package com.arsildo.core.preferences.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val datastoreModule = module {
    single {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile(name = "DATASTORE_FILE") },
        )
    }
}