package com.arsildo.prevail.logic.repository

import com.arsildo.prevail.logic.network.NetworkService
import com.arsildo.prevail.logic.network.model.thread_catalog.ThreadCatalogItem
import javax.inject.Inject

class BoardRepository @Inject constructor(
    private val networkService: NetworkService
) {
    suspend fun getCatalog(board:String): List<ThreadCatalogItem> {
        return networkService.getCatalog(board)
    }
}