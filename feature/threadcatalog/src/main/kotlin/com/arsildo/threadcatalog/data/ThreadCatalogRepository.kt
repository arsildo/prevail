package com.arsildo.threadcatalog.data

import ApiResult
import com.arsildo.model.ThreadCatalog
import com.arsildo.network.NetworkService

internal class ThreadCatalogRepository(
    private val networkService: NetworkService,
    private val board: String,
) {
    suspend fun getThreadCatalog(): ApiResult<List<ThreadCatalog>> {
        return ThreadCatalogDataSource(networkService, board).invoke()
    }
}