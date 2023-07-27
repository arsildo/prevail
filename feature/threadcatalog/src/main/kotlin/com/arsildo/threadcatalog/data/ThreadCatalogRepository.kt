package com.arsildo.threadcatalog.data

import ApiResult
import com.arsildo.model.ThreadCatalog
import com.arsildo.network.NetworkService

class ThreadCatalogRepository(
    private val networkService: NetworkService
) {
    suspend fun getThreadCatalog(board: String): ApiResult<List<ThreadCatalog>> {
        return ThreadCatalogDataSource(networkService).invoke(board)
    }
}