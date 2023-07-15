package com.arsildo.prevail.di

import com.arsildo.prevail.MainActivityViewModel
import com.arsildo.core.theme.ThemePreferencesRepository
import com.arsildo.threadcatalog.ThreadCatalogRepository
import com.arsildo.threadcatalog.ThreadsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::ThreadCatalogRepository)
    singleOf(::ThemePreferencesRepository)
    viewModelOf(::ThreadsViewModel)
    viewModelOf(::MainActivityViewModel)
}