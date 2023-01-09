package com.arsildo.prevail.data.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "board")
@Keep
data class Board(
    @PrimaryKey(autoGenerate = false) val board: String = "n/a",
    @ColumnInfo(name = "boardDesc") val meta_description: String = "no description",
    @ColumnInfo(name = "boardTitle") val title: String = "n/a",
)