package com.arsildo.prevail.preferences

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@Composable
fun AppearancesPreferences(navController: NavController) {

    val viewModel = hiltViewModel<AppearancesViewModel>()
    val followSystem by viewModel.getSystemColorScheme().collectAsState(initial = true)
    val colorScheme by viewModel.getColorScheme().collectAsState(initial = false)
    val dynamicColorScheme by viewModel.getDynamicColorScheme().collectAsState(initial = true)
    AnimateColorSchemeTransition {
        PreferenceDetailsWrapper(
            content = {
                Column {
                    PreferenceCategoryLabel(title = "Appearance", navController = navController)
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SettingRow(
                            settingTitle = "Follow System Theme",
                            settingDescription = "Automatically switch color scheme based on your system preferences.",
                            checked = followSystem,
                            enabled = true,
                            onCheckedChange = viewModel::setSystemColorScheme
                        )
                        SettingRow(
                            settingTitle = "Dark Theme",
                            settingDescription = "Color scheme",
                            checked = colorScheme,
                            enabled = !followSystem,
                            onCheckedChange = viewModel::setColorScheme
                        )
                        SettingRow(
                            settingTitle = "Material You",
                            settingDescription = "Dynamic colors from your wallpaper. Supported in Android Version 12 (API 31) and later.",
                            checked = dynamicColorScheme,
                            enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
                            onCheckedChange = viewModel::setDynamicColorScheme
                        )
                    }
                }
            }
        )
    }

}


@Composable
fun SettingRow(
    settingTitle: String,
    settingDescription: String,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.fillMaxWidth(.8f)) {
            Text(
                text = settingTitle,
                color = if (enabled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.tertiary.copy(.5f),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = settingDescription,
                color = if (enabled) MaterialTheme.colorScheme.tertiary
                else MaterialTheme.colorScheme.tertiary.copy(.5f),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
        )
    }

}


@Composable // This composable animates between theme transitions
fun AnimateColorSchemeTransition(content: @Composable () -> Unit) {
    val colors = MaterialTheme.colorScheme.copy(
        background = animateColorAsState(
            targetValue = MaterialTheme.colorScheme.background,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        ).value,
        primary = animateColorAsState(
            targetValue = MaterialTheme.colorScheme.primary,
            animationSpec = tween(easing = LinearEasing, delayMillis = 256)
        ).value,
        secondary = animateColorAsState(
            targetValue = MaterialTheme.colorScheme.primary,
            animationSpec = tween(easing = LinearOutSlowInEasing, delayMillis = 256)
        ).value,
    )
    MaterialTheme(colorScheme = colors, content = content)
}