package com.arsildo.prevail.data.source

import com.arsildo.prevail.data.models.Boards
import com.arsildo.prevail.data.models.ThreadCatalog
import com.arsildo.prevail.data.models.ThreadPosts
import javax.inject.Inject

class ContentRepository @Inject constructor(private val retroFitService: RetroFitService) {

    suspend fun getThreadCatalog(board: String): ThreadCatalog {
        return retroFitService.getCatalog(board = "$board/catalog.json")
    }

    suspend fun getThread(threadNumber: Int): ThreadPosts {
        return retroFitService.getThreadPosts(threadNumber = threadNumber)
    }

    suspend fun getBoards(): Boards {
        return retroFitService.getBoards()
    }
}
