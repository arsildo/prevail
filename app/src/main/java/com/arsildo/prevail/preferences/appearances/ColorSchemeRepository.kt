package com.arsildo.prevail.preferences.appearances

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.arsildo.prevail.preferences.appearances.ColorSchemeRepository.ColorSchemePreferencesKeys.AUTOMATIC_COLOR_SCHEME
import com.arsildo.prevail.preferences.appearances.ColorSchemeRepository.ColorSchemePreferencesKeys.COLOR_SCHEME
import com.arsildo.prevail.preferences.appearances.ColorSchemeRepository.ColorSchemePreferencesKeys.DYNAMIC_COLOR_SCHEME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ColorSchemeRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object ColorSchemePreferencesKeys {
        val AUTOMATIC_COLOR_SCHEME = booleanPreferencesKey("automatic_color_scheme")
        val COLOR_SCHEME = booleanPreferencesKey("color_scheme")
        val DYNAMIC_COLOR_SCHEME = booleanPreferencesKey("dynamic_color_scheme")
    }

    // Follow System Color Scheme
    val getSystemColorScheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUTOMATIC_COLOR_SCHEME] ?: true
    }

    suspend fun setFollowSystemColorScheme(automatic: Boolean) {
        dataStore.edit { preferences -> preferences[AUTOMATIC_COLOR_SCHEME] = automatic }
    }

    // Color Scheme
    val getColorScheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[COLOR_SCHEME] ?: false
    }

    suspend fun setColorScheme(darkMode: Boolean) {
        dataStore.edit { preferences -> preferences[COLOR_SCHEME] = darkMode }
    }

    // Dynamic Color Scheme [API 31+]
    val getDynamicColorScheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DYNAMIC_COLOR_SCHEME] ?: true
    }

    suspend fun setDynamicColorScheme(enabled: Boolean) {
        dataStore.edit { preferences -> preferences[DYNAMIC_COLOR_SCHEME] = enabled }
    }

}