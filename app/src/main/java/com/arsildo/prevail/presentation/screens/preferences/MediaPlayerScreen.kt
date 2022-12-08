package com.arsildo.prevail.presentation.screens.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import com.arsildo.prevail.logic.cache.MediaPlayerPreferenceKeys
import kotlinx.coroutines.launch

@Composable
fun MediaPlayerScreen(
    dataStore: MediaPlayerPreferenceKeys
) {


    val autoPlayVideos = dataStore.getVideoAutoPlay.collectAsState(initial = false).value
    val autoPlayAudios = dataStore.getAudioAutoPlay.collectAsState(initial = false).value

    val coroutineScope = rememberCoroutineScope()
    Column {
        SettingRow(
            checked = autoPlayVideos,
            onCheckedChange = {
                if (autoPlayVideos) coroutineScope.launch { dataStore.setVideoAutoPlay(false) }
                else coroutineScope.launch { dataStore.setVideoAutoPlay(true) }
            },
            enabled = true,
            title = "Autoplay",
            subtitle = "Play video media automatically when it becomes visible."
        )
        SettingRow(
            checked = autoPlayAudios,
            onCheckedChange = {
                if (autoPlayAudios) coroutineScope.launch { dataStore.setAudioAutoPlay(false) }
                else coroutineScope.launch { dataStore.setAudioAutoPlay(true) }
            },
            enabled = true,
            title = "Play with audio",
            subtitle = "Enable audio automatically when playing videos."
        )

    }
}
