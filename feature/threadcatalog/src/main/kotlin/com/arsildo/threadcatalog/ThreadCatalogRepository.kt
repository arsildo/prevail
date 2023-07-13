package com.arsildo.threadcatalog

import com.arsildo.model.ThreadCatalogItem
import com.arsildo.network.NetworkService

class ThreadCatalogRepository(
    private val networkService: NetworkService
) {
    suspend fun getThreadCatalog(board: String): List<ThreadCatalogItem> {
        return networkService.getThreadCatalog(board)
    }
}