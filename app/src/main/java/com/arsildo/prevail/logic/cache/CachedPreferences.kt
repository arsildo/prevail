package com.arsildo.prevail.logic.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.arsildo.prevail.logic.constants.DATASTORE_KEY
import com.arsildo.prevail.logic.constants.DYNAMIC_THEME_PREF_KEY
import com.arsildo.prevail.logic.constants.FOLLOW_SYSTEM_THEME_KEY
import com.arsildo.prevail.logic.constants.THEME_PREF_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CachedPreferences(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_KEY)

        val FOLLOW_SYSTEM_THEME_PREF = booleanPreferencesKey(FOLLOW_SYSTEM_THEME_KEY)
        val THEME_PREF = booleanPreferencesKey(THEME_PREF_KEY)
        val IS_DYNAMIC_THEME_ENABLED = booleanPreferencesKey(DYNAMIC_THEME_PREF_KEY)

    }

    val getFollowSystemThemePreference: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[FOLLOW_SYSTEM_THEME_PREF] ?: false
    }
    suspend fun setFollowSystemThemePreference(automatic: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[FOLLOW_SYSTEM_THEME_PREF] = automatic
        }
    }


    val getThemePreference: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[THEME_PREF] ?: false
    }
    suspend fun setThemePreference(dark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_PREF] = dark
        }
    }


    val getDynamicColorSchemePreference: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_DYNAMIC_THEME_ENABLED] ?: true
    }
    suspend fun setDynamicColorSchemePreference(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DYNAMIC_THEME_ENABLED] = enabled
        }
    }

}