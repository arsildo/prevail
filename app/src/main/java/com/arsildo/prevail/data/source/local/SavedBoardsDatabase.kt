package com.arsildo.prevail.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arsildo.prevail.data.models.Board

@Database(entities = [Board::class], version = 1, exportSchema = false)
abstract class SavedBoardsDatabase : RoomDatabase() {
    abstract fun boardDao(): SavedBoardsDao
}