package com.arsildo.prevail.preferences.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.arsildo.prevail.preferences.PreferenceCategoryLabel
import com.arsildo.prevail.preferences.PreferenceDetailsWrapper
import com.arsildo.prevail.preferences.utils.SettingRow

@Composable
fun PlayerPreferencesScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<PlayerViewModel>()
    val autoPlayVideos by viewModel.getAutoPlayMedia().collectAsState(initial = true)
    val playMuted by viewModel.getPlayMediaMuted().collectAsState(initial = false)

    PreferenceDetailsWrapper(
        content = {
            Column {
                PreferenceCategoryLabel(title = "Media Player", navController = navController)
                Column(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SettingRow(
                        settingTitle = "Autoplay media",
                        settingDescription = "Play Media when in focus.",
                        checked = autoPlayVideos,
                        enabled = true,
                        onCheckedChange = viewModel::setAutoPlayMedia
                    )
                    SettingRow(
                        settingTitle = "Play media muted",
                        settingDescription = "Play Media with no audio at first.",
                        checked = playMuted,
                        enabled = true,
                        onCheckedChange = viewModel::setPlayMediaMuted
                    )
                }
            }
        }
    )

}