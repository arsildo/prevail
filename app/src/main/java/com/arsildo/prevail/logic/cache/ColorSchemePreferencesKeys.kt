package com.arsildo.prevail.logic.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ColorSchemePreferencesKeys(private val context: Context) {
    companion object {

        private val Context.dataStore: DataStore<Preferences>
                by preferencesDataStore(COLOR_SCHEME_SETTINGS_KEY)

        val SYSTEM_COLOR_SCHEME = booleanPreferencesKey(FOLLOW_SYSTEM_COLOR_SCHEME_KEY)
        val COLOR_SCHEME = booleanPreferencesKey(COLOR_SCHEME_KEY)
        val DYNAMIC_COLOR_SCHEME = booleanPreferencesKey(DYNAMIC_COLOR_SCHEME_KEY)

    }

    // Follow System Color Scheme
    val getSystemColorScheme: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SYSTEM_COLOR_SCHEME] ?: true
    }

    suspend fun setSystemColorScheme(automatic: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SYSTEM_COLOR_SCHEME] = automatic
        }
    }

    // Color Scheme
    val getColorScheme: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[COLOR_SCHEME] ?: false
    }

    suspend fun setColorScheme(dark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[COLOR_SCHEME] = dark
        }
    }

    // Dynamic Color Scheme
    val getDynamicColorScheme: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DYNAMIC_COLOR_SCHEME] ?: true
    }

    suspend fun setDynamicColorScheme(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DYNAMIC_COLOR_SCHEME] = enabled
        }
    }


}