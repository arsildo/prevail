package com.arsildo.prevail.feature.boards.data

import ApiResult
import com.arsildo.model.Boards
import com.arsildo.network.NetworkService
import handleApi

class BoardsDataSource(private val networkService: NetworkService) {
    suspend operator fun invoke(): ApiResult<Boards> =
        handleApi { networkService.getBoards() }
}