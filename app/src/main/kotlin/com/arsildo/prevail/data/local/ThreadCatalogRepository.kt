package com.arsildo.prevail.data.local

import com.arsildo.prevail.data.ThreadCatalogItem
import com.arsildo.prevail.data.remote.NetworkService

class ThreadCatalogRepository (
    private val networkService: NetworkService
){
    suspend fun getThreadCatalog(board: String): List<ThreadCatalogItem> {
        return networkService.getThreadCatalog(board)
    }
}