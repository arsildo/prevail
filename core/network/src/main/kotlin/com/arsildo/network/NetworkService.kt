package com.arsildo.network

import com.arsildo.model.Post
import com.arsildo.model.Posts
import com.arsildo.model.ThreadCatalog
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface NetworkService {
    @GET
    suspend fun getThreadCatalog(@Url board: String): Response<List<ThreadCatalog>>

    @GET("{thread}/thread/{threadNumber}.json")
    suspend fun getPosts(
        @Path("thread") thread: String,
        @Path("threadNumber") threadNumber: Int
    ): Response<Posts>

}