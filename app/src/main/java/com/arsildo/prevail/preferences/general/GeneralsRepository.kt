package com.arsildo.prevail.preferences.general

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.arsildo.prevail.preferences.general.GeneralsRepository.GeneralsPreferenceKeys.CONFIRM_APP_EXIT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GeneralsRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private object GeneralsPreferenceKeys {
        val CONFIRM_APP_EXIT = booleanPreferencesKey("confirm_app_exit")
    }

    // Follow System Color Scheme
    val getConfirmAppExit: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[CONFIRM_APP_EXIT] ?: true
    }

    suspend fun setConfirmAppExit(enabled: Boolean) {
        dataStore.edit { preferences -> preferences[CONFIRM_APP_EXIT] = enabled }
    }
}