package com.arsildo.prevail.data.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.arsildo.prevail.data.source.BoardPreferencesRepository.BoardPreferencesKeys.CURRENT_BOARD
import com.arsildo.prevail.data.source.BoardPreferencesRepository.BoardPreferencesKeys.CURRENT_BOARD_DESCRIPTION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


const val NO_BOARD = "no board"
const val NO_BOARD_DESC = "no board desc"

class BoardPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object BoardPreferencesKeys {
        val CURRENT_BOARD = stringPreferencesKey("last_board")
        val CURRENT_BOARD_DESCRIPTION = stringPreferencesKey("last_board_description")
    }


    // Current Selected Board
    val getCurrentBoard: Flow<String> = dataStore.data.map { preferences ->
        preferences[CURRENT_BOARD] ?: NO_BOARD
    }

    suspend fun setCurrentBoard(board: String) {
        dataStore.edit { preferences -> preferences[CURRENT_BOARD] = board }
    }

    // Current Selected Board Desc
    val getCurrentBoardDescription: Flow<String> = dataStore.data.map { preferences ->
        preferences[CURRENT_BOARD_DESCRIPTION] ?: NO_BOARD_DESC
    }

    suspend fun setCurrentBoardDescription(boardDesc: String) {
        dataStore.edit { preferences -> preferences[CURRENT_BOARD_DESCRIPTION] = boardDesc }
    }

}