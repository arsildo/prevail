package com.arsildo.threadcatalog

import com.arsildo.model.ThreadCatalog
import com.arsildo.network.NetworkService
import org.koin.core.annotation.Single

@Single
class ThreadCatalogRepository(
    private val networkService: NetworkService
) {
    suspend fun getThreadCatalog(board: String): List<ThreadCatalog> {
        return networkService.getThreadCatalog("$board/catalog.json")
    }
}