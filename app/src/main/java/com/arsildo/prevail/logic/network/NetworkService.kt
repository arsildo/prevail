package com.arsildo.prevail.logic.network

import com.arsildo.prevail.logic.network.models.boards.Boards
import com.arsildo.prevail.logic.network.models.threads.ThreadCatalogItem
import retrofit2.http.GET
import retrofit2.http.Url

interface NetworkService {

    @GET
    suspend fun getCatalog(@Url board: String): List<ThreadCatalogItem>

    @GET("boards.json")
    suspend fun getBoards(): Boards

}