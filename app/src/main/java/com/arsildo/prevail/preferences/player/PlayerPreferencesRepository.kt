package com.arsildo.prevail.preferences.player

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.arsildo.prevail.preferences.player.PlayerPreferencesRepository.PlayerPreferencesKeys.AUTOPLAY
import com.arsildo.prevail.preferences.player.PlayerPreferencesRepository.PlayerPreferencesKeys.PLAY_MUTED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object PlayerPreferencesKeys {
        val AUTOPLAY = booleanPreferencesKey("autoplay")
        val PLAY_MUTED = booleanPreferencesKey("play_muted")
    }

    // Autoplay Media
    val getAutoPlayMedia: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUTOPLAY] ?: true
    }

    suspend fun setAutoPlayMedia(autoplay: Boolean) {
        dataStore.edit { preferences -> preferences[AUTOPLAY] = autoplay }
    }


    // Play Media Muted
    val getPlayMediaMuted: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PLAY_MUTED] ?: false
    }

    suspend fun setPlayMediaMuted(muted: Boolean) {
        dataStore.edit { preferences -> preferences[PLAY_MUTED] = muted }
    }

}