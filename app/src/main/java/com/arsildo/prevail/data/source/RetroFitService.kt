package com.arsildo.prevail.data.source

import androidx.annotation.Keep
import com.arsildo.prevail.data.models.Boards
import com.arsildo.prevail.data.models.ThreadCatalog
import com.arsildo.prevail.data.models.ThreadPosts
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


@Keep
interface RetroFitService {

    @GET
    suspend fun getCatalog(@Url board: String): ThreadCatalog

    @GET("{currentThread}/thread/{threadNumber}.json")
    suspend fun getThreadPosts(
        @Path("currentThread") currentThread: String,
        @Path("threadNumber") threadNumber: Int
    ): ThreadPosts

    @GET("boards.json")
    suspend fun getBoards(): Boards

}