package com.arsildo.prevail.preferences.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arsildo.prevail.preferences.PreferenceCategoryLabel
import com.arsildo.prevail.preferences.PreferenceDetailsWrapper
import com.arsildo.prevail.preferences.utils.SettingRow

@Composable
fun GeneralPreferencesScreen(navController: NavController) {
    val viewModel = hiltViewModel<GeneralsViewModel>()
    val confirmAppExit by viewModel.getConfirmAppExit().collectAsState(initial = true)
    PreferenceDetailsWrapper(
        content = {
            Column {
                PreferenceCategoryLabel(title = "General", navController = navController)
                Column(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SettingRow(
                        settingTitle = "Confim app exit.",
                        settingDescription = "Swipe back twice to leave app.",
                        checked = confirmAppExit,
                        enabled = true,
                        onCheckedChange = viewModel::setConfirmAppExit
                    )
                }
            }
        }
    )
}