package com.arsildo.prevail.feature.boards.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class FavoriteBoardRepository(
    private val dataStore : DataStore<Preferences>
) {
    companion object {
        val FAVORITE_BOARD = stringPreferencesKey("favorite_board")

    }

    val getFavoriteBoard: Flow<String> = dataStore.data.map { preferences ->
        preferences[FAVORITE_BOARD] ?: "po"
    }

    suspend fun setFavoriteBoard(board: String) {
        dataStore.edit { preferences ->
            preferences[FAVORITE_BOARD] = board
        }
    }
}