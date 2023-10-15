package com.arsildo.threadcatalog.di

import com.arsildo.threadcatalog.ThreadCatalogViewModel
import com.arsildo.threadcatalog.data.ThreadCatalogDataSource
import com.arsildo.threadcatalog.data.ThreadCatalogRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val threadCatalogModule = module {
    singleOf(::ThreadCatalogDataSource)
    singleOf(::ThreadCatalogRepository)
    viewModelOf(::ThreadCatalogViewModel)
}