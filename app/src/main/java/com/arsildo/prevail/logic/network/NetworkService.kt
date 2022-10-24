package com.arsildo.prevail.logic.network

import com.arsildo.prevail.logic.constants.BOARDS_ENDPOINT
import com.arsildo.prevail.logic.network.model.BoardList
import retrofit2.http.GET

interface NetworkService {
    @GET(BOARDS_ENDPOINT)
    suspend fun getBoards(): BoardList
}