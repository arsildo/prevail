package com.arsildo.prevail.data.source

import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject

class PlayerPreferencesRepository @Inject constructor() {

    private object PlayerPreferencesKeys {
        val AUTOPLAY = stringPreferencesKey("autoplay")
        val PLAY_MUTED = stringPreferencesKey("play_muted")
    }


}