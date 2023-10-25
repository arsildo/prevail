package com.arsildo.threadcatalog.data

import ApiResult
import com.arsildo.model.ThreadCatalog
import com.arsildo.network.NetworkService
import com.arsildo.prevail.feature.boards.data.LastVisitedBoardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal class ThreadCatalogRepository(
    private val networkService: NetworkService,
    private val lastVisitedBoardRepository: LastVisitedBoardRepository
) {
    suspend fun getThreadCatalog(): ApiResult<List<ThreadCatalog>> {
        return ThreadCatalogDataSource(networkService, runBlocking { lastVisitedBoardRepository.getLastVisitedBoard.first() }).invoke()
    }
}