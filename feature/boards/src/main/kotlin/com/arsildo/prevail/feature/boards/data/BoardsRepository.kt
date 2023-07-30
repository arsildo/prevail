package com.arsildo.prevail.feature.boards.data

import ApiResult
import com.arsildo.model.Boards
import com.arsildo.network.NetworkService

class BoardsRepository(private val networkService: NetworkService) {
    suspend fun getBoards(): ApiResult<Boards> {
        return BoardsDataSource(networkService).invoke()
    }
}