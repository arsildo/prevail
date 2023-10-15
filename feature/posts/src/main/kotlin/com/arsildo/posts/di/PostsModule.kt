package com.arsildo.posts.di

import com.arsildo.posts.PostsViewModel
import com.arsildo.posts.data.PostsDataSource
import com.arsildo.posts.data.PostsRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val postsModule = module {
    singleOf(::PostsDataSource)
    singleOf(::PostsRepository)
    viewModelOf(::PostsViewModel)
}