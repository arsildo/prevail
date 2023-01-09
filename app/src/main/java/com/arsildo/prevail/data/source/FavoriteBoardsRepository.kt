package com.arsildo.prevail.data.source

import androidx.lifecycle.LiveData
import com.arsildo.prevail.data.models.Board
import com.arsildo.prevail.data.source.local.SavedBoardsDao
import javax.inject.Inject

class FavoriteBoardsRepository @Inject constructor(private val dao: SavedBoardsDao) {

    fun getAllFavoriteBoards(): LiveData<List<Board>> {
        return dao.getAllBoards()
    }

    fun insertFavoriteBoard(board: Board) {
        dao.insertFavoriteBoard(board)
    }

    fun removeFavoriteBoard(board: Board) {
        return dao.removeFavoriteBoard(board)
    }

    fun deleteSavedBoards() {
        return dao.deleteSavedBoards()
    }
}
