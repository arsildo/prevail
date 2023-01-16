package com.arsildo.prevail.data.source

import com.arsildo.prevail.data.Boards
import com.arsildo.prevail.data.ThreadCatalog
import com.arsildo.prevail.data.ThreadPosts
import com.arsildo.prevail.data.source.remote.RetroFitService
import javax.inject.Inject

class ContentRepository @Inject constructor(private val retroFitService: RetroFitService) {

    suspend fun getThreadCatalog(board: String): ThreadCatalog {
        return retroFitService.getCatalog(board = "$board/catalog.json")
    }

    suspend fun getThread(currentThread: String, threadNumber: Int): ThreadPosts {
        return retroFitService.getThreadPosts(
            currentThread = currentThread,
            threadNumber = threadNumber
        )
    }

    suspend fun getBoards(): Boards {
        return retroFitService.getBoards()
    }
}
