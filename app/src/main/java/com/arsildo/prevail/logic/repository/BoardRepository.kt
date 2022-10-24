package com.arsildo.prevail.logic.repository

import com.arsildo.prevail.logic.network.NetworkService
import com.arsildo.prevail.logic.network.model.BoardList
import javax.inject.Inject

class BoardRepository @Inject constructor(
    private val networkService: NetworkService
) {
    suspend fun getBoards(): BoardList {
        return networkService.getBoards()
    }
}