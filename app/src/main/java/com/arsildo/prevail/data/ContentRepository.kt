package com.arsildo.prevail.data

import javax.inject.Inject

class ContentRepository @Inject constructor(private val retroFitService: RetroFitService) {

    suspend fun getThreadCatalog(board: String): ThreadCatalog {
        return retroFitService.getCatalog(board)
    }

    suspend fun getThread(thread: Int): ThreadPosts {
        return retroFitService.getThreadPosts(thread)
    }

    suspend fun getBoards(): Boards {
        return retroFitService.getBoards()
    }
}
