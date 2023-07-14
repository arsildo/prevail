package com.arsildo.prevail.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.arsildo.prevail.MainActivityViewModel
import com.arsildo.prevail.data.local.PreferencesRepository
import com.arsildo.threadcatalog.ThreadCatalogRepository
import com.arsildo.threadcatalog.ThreadsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::ThreadCatalogRepository)
    singleOf(::PreferencesRepository)
    viewModelOf(::ThreadsViewModel)
    viewModelOf(::MainActivityViewModel)
}