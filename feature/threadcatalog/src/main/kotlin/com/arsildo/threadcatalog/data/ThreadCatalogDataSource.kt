package com.arsildo.threadcatalog.data

import ApiResult
import com.arsildo.model.ThreadCatalog
import com.arsildo.network.NetworkService
import handleApi

class ThreadCatalogDataSource(private val networkService: NetworkService) {
    suspend operator fun invoke(board: String): ApiResult<List<ThreadCatalog>> =
        handleApi { networkService.getThreadCatalog("$board/catalog.json") }
}
