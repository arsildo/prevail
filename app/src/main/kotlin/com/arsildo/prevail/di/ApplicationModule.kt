package com.arsildo.prevail.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import coil.ImageLoaderFactory
import com.arsildo.prevail.MainActivityViewModel
import com.arsildo.prevail.data.local.PreferencesRepository
import com.arsildo.prevail.data.local.ThreadCatalogRepository
import com.arsildo.prevail.data.remote.NetworkService
import com.arsildo.prevail.threads.ThreadsViewModel
import com.arsildo.prevail.utils.API_BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {

    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    single { get<Retrofit>().create(NetworkService::class.java) }
    singleOf(::ThreadCatalogRepository)

    single {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile("DATASTORE_KEY") },
        )
    }

    singleOf(::PreferencesRepository)
    viewModelOf(::ThreadsViewModel)
    viewModelOf(::MainActivityViewModel)

    singleOf(::ImageLoaderFactory) { bind() }
}