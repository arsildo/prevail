package com.arsildo.prevail.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arsildo.prevail.data.models.Board

@Dao
interface SavedBoardsDao {

    @Query("SELECT * FROM board")
    fun getAllBoards(): LiveData<List<Board>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteBoard(board: Board)

    @Delete
    fun removeFavoriteBoard(board: Board)

    @Query("DELETE FROM board")
    fun deleteSavedBoards()
}