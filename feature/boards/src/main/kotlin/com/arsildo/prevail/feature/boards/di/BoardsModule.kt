package com.arsildo.prevail.feature.boards.di

import com.arsildo.prevail.feature.boards.data.LastVisitedBoardRepository
import com.arsildo.prevail.feature.boards.BoardsViewModel
import com.arsildo.prevail.feature.boards.data.BoardsDataSource
import com.arsildo.prevail.feature.boards.data.BoardsRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val boardsModule = module {
    singleOf(::BoardsDataSource)
    singleOf(::BoardsRepository)
    singleOf(::LastVisitedBoardRepository)
    viewModelOf(::BoardsViewModel)
}