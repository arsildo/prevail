package com.arsildo.threadcatalog.data

import ApiResult
import com.arsildo.model.ThreadCatalog
import com.arsildo.network.NetworkService
import handleApi

internal class ThreadCatalogDataSource(
    private val networkService: NetworkService,
    private val board: String,
) {
    suspend operator fun invoke(): ApiResult<List<ThreadCatalog>> =
        handleApi { networkService.getThreadCatalog(board = "$board/catalog.json") }
}
