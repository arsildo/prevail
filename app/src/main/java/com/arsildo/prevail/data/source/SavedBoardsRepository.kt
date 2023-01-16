package com.arsildo.prevail.data.source

import androidx.lifecycle.LiveData
import com.arsildo.prevail.data.Board
import com.arsildo.prevail.data.source.local.SavedBoardsDao
import javax.inject.Inject

class SavedBoardsRepository @Inject constructor(private val dao: SavedBoardsDao) {

    fun getSavedBoards(): LiveData<List<Board>> {
        return dao.getAllBoards()
    }

    fun insertToSavedBoards(board: Board) {
        dao.insertToSavedBoards(board)
    }

    fun removeFromSavedBoards(board: Board) {
        return dao.removeFromSavedBoards(board)
    }

    fun deleteAllSavedBoards() {
        return dao.deleteAllSavedBoards()
    }
}
