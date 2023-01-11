package com.arsildo.prevail.data.source

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.arsildo.prevail.data.source.BoardPreferencesRepository.BoardPreferencesKeys.LAST_BOARD
import com.arsildo.prevail.data.source.BoardPreferencesRepository.BoardPreferencesKeys.LAST_BOARD_DESCRIPTION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BoardPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object BoardPreferencesKeys {
        val LAST_BOARD = stringPreferencesKey("last_board")
        val LAST_BOARD_DESCRIPTION = stringPreferencesKey("last_board_description")
    }

    var currentBoard = mutableStateOf("empty")
    var currentBoardDesc = mutableStateOf("empty")

    // Last Board
    val getLastBoard: Flow<String> = dataStore.data.map { preferences ->
        preferences[LAST_BOARD] ?: "empty"
    }

    suspend fun setLastBoard(board: String) {
        dataStore.edit { preferences -> preferences[LAST_BOARD] = board }
    }

    // Last Board Desc
    val getLastBoardDescription: Flow<String> = dataStore.data.map { preferences ->
        preferences[LAST_BOARD_DESCRIPTION] ?: "empty"
    }

    suspend fun setLastBoardDescription(boardDesc: String) {
        dataStore.edit { preferences -> preferences[LAST_BOARD_DESCRIPTION] = boardDesc }
    }

}