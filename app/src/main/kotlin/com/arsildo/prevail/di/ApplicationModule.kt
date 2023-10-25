package com.arsildo.prevail.di

import com.arsildo.core.preferences.di.datastoreModule
import com.arsildo.core.theme.MainActivityViewModel
import com.arsildo.core.theme.ThemePreferencesRepository
import com.arsildo.network.di.networkModule
import com.arsildo.posts.di.postsModule
import com.arsildo.preferences.appearance.AppearancePreferencesViewModel
import com.arsildo.prevail.feature.boards.di.boardsModule
import com.arsildo.threadcatalog.di.threadCatalogModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::ThemePreferencesRepository)

    viewModelOf(::MainActivityViewModel)
    viewModelOf(::AppearancePreferencesViewModel)

    includes(
        threadCatalogModule,
        postsModule,
        boardsModule
    )

    includes(
        networkModule,
        datastoreModule
    )

}