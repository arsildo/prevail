package com.arsildo.prevail.data

import androidx.annotation.Keep
import com.arsildo.prevail.di.CURRENT_BOARD
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

    @GET("$CURRENT_BOARD/thread/{threadNumber}.json")
    suspend fun getThreadPosts(@Path("threadNumber") threadNumber: Int): ThreadPosts

    @GET("boards.json")
    suspend fun getBoards(): Boards

}