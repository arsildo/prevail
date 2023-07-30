package com.arsildo.prevail.di

import com.arsildo.core.theme.ThemePreferencesRepository
import com.arsildo.posts.PostsViewModel
import com.arsildo.posts.data.PostsDataSource
import com.arsildo.posts.data.PostsRepository
import com.arsildo.preferences.appearance.AppearancePreferencesViewModel
import com.arsildo.prevail.MainActivityViewModel
import com.arsildo.prevail.feature.boards.BoardsViewModel
import com.arsildo.prevail.feature.boards.data.BoardsDataSource
import com.arsildo.prevail.feature.boards.data.BoardsRepository
import com.arsildo.threadcatalog.ThreadsViewModel
import com.arsildo.threadcatalog.data.ThreadCatalogDataSource
import com.arsildo.threadcatalog.data.ThreadCatalogRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::ThemePreferencesRepository)

    singleOf(::ThreadCatalogDataSource)
    singleOf(::ThreadCatalogRepository)
    viewModelOf(::ThreadsViewModel)

    singleOf(::PostsDataSource)
    singleOf(::PostsRepository)
    viewModelOf(::PostsViewModel)

    singleOf(::BoardsDataSource)
    singleOf(::BoardsRepository)
    viewModelOf(::BoardsViewModel)

    viewModelOf(::MainActivityViewModel)
    viewModelOf(::AppearancePreferencesViewModel)
}