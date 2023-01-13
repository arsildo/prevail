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


@Composable
fun AppearancesPreferences() {

    val viewModel = hiltViewModel<AppearancesViewModel>()
    val followSystem by viewModel.getSystemColorScheme().collectAsState(initial = true)
    val colorScheme by viewModel.getColorScheme().collectAsState(initial = false)
    val dynamicColorScheme by viewModel.getDynamicColorScheme().collectAsState(initial = true)

    AnimateColorSchemeTransition {
        Column {
            SettingRow(
                checked = followSystem,
                onCheckedChange = { viewModel.setSystemColorScheme(it) },
                enabled = true,
                title = "Follow System Theme",
                subtitle = "Automatically switch color scheme based on your system preferences."
            )
            SettingRow(
                checked = colorScheme,
                onCheckedChange = { viewModel.setColorScheme(it) },
                enabled = !followSystem,
                title = "Dark Theme",
                subtitle = "Color scheme"
            )
            SettingRow(
                checked = dynamicColorScheme,
                onCheckedChange = { viewModel.setDynamicColorScheme(it) },
                enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
                title = "Material You",
                subtitle = "Dynamic colors from your wallpaper. Only supported in Android Version 12 (API 31) and later."
            )
        }
    }
}


@Composable
fun SettingRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean,
    title: String,
    subtitle: String,
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.fillMaxWidth(.8f)) {
            Text(
                text = title,
                color = if (enabled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.tertiary.copy(.5f),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = subtitle,
                color = if (enabled) MaterialTheme.colorScheme.primary
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

@Composable
fun AnimateColorSchemeTransition(content: @Composable () -> Unit) {
    val colors = MaterialTheme.colorScheme.copy(
        background = animateColorAsState(
            targetValue = MaterialTheme.colorScheme.background,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        ).value,
        primary = animateColorAsState(
            targetValue = MaterialTheme.colorScheme.primary,
            animationSpec = tween(easing = LinearEasing)
        ).value,
        secondary = animateColorAsState(
            targetValue = MaterialTheme.colorScheme.primary,
            animationSpec = tween(easing = LinearOutSlowInEasing)
        ).value,
    )
    MaterialTheme(colorScheme = colors, content = content)
}