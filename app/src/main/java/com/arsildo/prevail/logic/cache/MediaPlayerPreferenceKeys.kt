package com.arsildo.prevail.logic.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaPlayerPreferenceKeys(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences>
                by preferencesDataStore(MEDIA_PLAYER_SETTINGS_KEY)

        val AUTOPLAY_VIDEOS = booleanPreferencesKey(AUTOPLAY_VIDEO_KEY)
        val AUTOPLAY_VIDEOS_AUDIO = booleanPreferencesKey(AUTOPLAY_AUDIO_KEY)
    }

    // Autoplay Videos
    val getVideoAutoPlay: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[AUTOPLAY_VIDEOS] ?: false
    }

    suspend fun setVideoAutoPlay(autoPlay: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTOPLAY_VIDEOS] = autoPlay
        }
    }

    // Autoplay Audio
    val getAudioAutoPlay: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[AUTOPLAY_VIDEOS_AUDIO] ?: false
    }

    suspend fun setAudioAutoPlay(autoplayWithAudio: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTOPLAY_VIDEOS_AUDIO] = autoplayWithAudio
        }
    }
}