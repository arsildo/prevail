package com.arsildo.posts.data

import ApiResult
import com.arsildo.model.Posts
import com.arsildo.network.NetworkService
import com.arsildo.prevail.feature.boards.data.LastVisitedBoardRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

internal class PostsRepository(
    private val networkService: NetworkService,
    private val lastVisitedBoardRepository: LastVisitedBoardRepository
) {
    suspend fun getThreadCatalog(
        threadNumber: Int
    ): ApiResult<Posts> {
        return PostsDataSource(networkService).invoke(
            thread = runBlocking { lastVisitedBoardRepository.getLastVisitedBoard.first() },
            threadNumber = threadNumber
        )
    }
}