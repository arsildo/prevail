package com.arsildo.posts.data

import ApiResult
import com.arsildo.model.Posts
import com.arsildo.network.NetworkService

class PostsRepository(
    private val networkService: NetworkService
) {
    suspend fun getThreadCatalog(
        thread: String,
        threadNumber: Int,
    ): ApiResult<Posts> {
        return PostsDataSource(networkService).invoke(
            thread = thread,
            threadNumber = threadNumber
        )
    }
}