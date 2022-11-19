package com.arsildo.prevail.logic.network

import androidx.annotation.Keep
import com.arsildo.prevail.logic.network.models.boards.Boards
import com.arsildo.prevail.logic.network.models.thread.ThreadPosts
import com.arsildo.prevail.logic.network.models.threads.ThreadCatalog
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


@Keep
interface NetworkService {
    @GET
    suspend fun getCatalog(@Url board: String): ThreadCatalog

    @GET("wsg/thread/{threadNumber}.json")
    suspend fun getThread(@Path("threadNumber") thread: Int) : ThreadPosts


    @GET("boards.json")
    suspend fun getBoards(): Boards

}