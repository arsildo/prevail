package com.arsildo.core.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.arsildo.core.preferences.di.datastoreModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import org.koin.core.annotation.Singleton

class ThemePreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val AUTOMATIC_THEME = booleanPreferencesKey("automatic_theme")
        val THEME_PREFERENCE = booleanPreferencesKey("manual_theme")
    }

    val isAutomaticThemeEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUTOMATIC_THEME] ?: true
    }

    suspend fun setAutomaticTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTOMATIC_THEME] = enabled
        }
    }

    val getTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[THEME_PREFERENCE] ?: false
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_PREFERENCE] = enabled
        }
    }

}