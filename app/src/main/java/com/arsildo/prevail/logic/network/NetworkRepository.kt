package com.arsildo.prevail.logic.network

import com.arsildo.prevail.logic.network.models.boards.Board
import com.arsildo.prevail.logic.network.models.boards.Boards
import com.arsildo.prevail.logic.network.models.threads.ThreadCatalogItem
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val networkService: NetworkService
) {

    suspend fun getBoards(): Boards {
        return networkService.getBoards()
    }

    suspend fun getThreadCatalog(board: String): List<ThreadCatalogItem> {
        return networkService.getCatalog(board)
    }
}
