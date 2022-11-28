package com.arsildo.prevail.presentation.screens.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun MediaPlayerScreen() {

    Column {
        SettingRow(
            checked = true,
            onCheckedChange = {},
            enabled = true,
            title = "Autoplay",
            subtitle = "Play video automatically when it becomes visible."
        )
        SettingRow(
            checked = true,
            onCheckedChange = {},
            enabled = true,
            title = "Play with audio",
            subtitle = "Enable audio automatically when playing videos."
        )

    }
}
