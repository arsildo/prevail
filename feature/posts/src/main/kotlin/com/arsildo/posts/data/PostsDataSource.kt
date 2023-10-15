package com.arsildo.posts.data

import ApiResult
import com.arsildo.model.Post
import com.arsildo.model.Posts
import com.arsildo.network.NetworkService
import handleApi

internal class PostsDataSource(private val networkService: NetworkService) {
    suspend operator fun invoke(
        thread: String,
        threadNumber: Int
    ): ApiResult<Posts> =
        handleApi {
            networkService.getPosts(
                thread = thread,
                threadNumber = threadNumber
            )
        }
}