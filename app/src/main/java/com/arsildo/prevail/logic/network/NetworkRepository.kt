package com.arsildo.prevail.logic.network

import com.arsildo.prevail.logic.network.models.boardList.Boards
import com.arsildo.prevail.logic.network.models.threadPosts.ThreadPosts
import com.arsildo.prevail.logic.network.models.threadList.ThreadCatalog
import javax.inject.Inject

class NetworkRepository @Inject constructor(private val networkService: NetworkService) {

    suspend fun getThreadCatalog(board: String): ThreadCatalog {
        return networkService.getCatalog(board)
    }

    suspend fun getThread(thread: Int): ThreadPosts {
        return networkService.getThread(thread)
    }

    suspend fun getBoards(): Boards {
        return networkService.getBoards()
    }
}
