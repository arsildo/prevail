package com.arsildo.threadcatalog

import ApiResult
import com.arsildo.model.ThreadCatalog
import com.arsildo.network.NetworkService
import handleApi


class ThreadCatalogDataSource(private val networkService: NetworkService) {
    suspend operator fun invoke(board: String): ApiResult<List<ThreadCatalog>> =
        handleApi { networkService.getThreadCatalog("$board/catalog.json") }
}

class ThreadCatalogRepository(
    private val networkService: NetworkService
) {
    suspend fun getThreadCatalog(board: String): ApiResult<List<ThreadCatalog>> {
        return ThreadCatalogDataSource(networkService).invoke(board)
    }
}