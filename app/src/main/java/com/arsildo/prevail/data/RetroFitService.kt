package com.arsildo.prevail.data

import androidx.annotation.Keep
import com.arsildo.prevail.di.CURRENT_BOARD
import com.arsildo.prevail.data.Boards
import com.arsildo.prevail.data.ThreadCatalog
import com.arsildo.prevail.data.ThreadPosts
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


@Keep
interface RetroFitService {
    @GET
    suspend fun getCatalog(@Url board: String): ThreadCatalog

    @GET("$CURRENT_BOARD/thread/{threadNumber}.json")
    suspend fun getThreadPosts(@Path("threadNumber") thread: Int): ThreadPosts

    @GET("boards.json")
    suspend fun getBoards(): Boards

}