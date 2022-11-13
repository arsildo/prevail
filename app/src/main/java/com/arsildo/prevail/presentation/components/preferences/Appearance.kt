package com.arsildo.prevail.presentation.components.preferences

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arsildo.prevail.logic.cache.ColorSchemePreferences
import kotlinx.coroutines.launch


@Composable
fun Appearance(
    dataStore: ColorSchemePreferences
) {
    val automaticTheme = dataStore.getSystemColorScheme.collectAsState(initial = true).value
    val theme = dataStore.getColorScheme.collectAsState(initial = isSystemInDarkTheme()).value
    val dynamicColorScheme = dataStore.getDynamicColorScheme.collectAsState(initial = true).value

    val coroutineScope = rememberCoroutineScope()
    AnimateColorSchemeChange {
        Column {
            SettingRow(
                checked = automaticTheme,
                onCheckedChange = {
                    if (automaticTheme)
                        coroutineScope.launch { dataStore.setSystemColorScheme(false) }
                    else coroutineScope.launch { dataStore.setSystemColorScheme(true) }
                },
                enabled = true,
                title = "Follow System Theme",
                subtitle = "Automatically switch color scheme based on your system preferences."
            )
            SettingRow(
                checked = theme,
                onCheckedChange = {
                    if (theme)
                        coroutineScope.launch { dataStore.setColorScheme(false) }
                    else coroutineScope.launch { dataStore.setColorScheme(true) }
                },
                enabled = !automaticTheme,
                title = "Dark Theme",
                subtitle = "Color scheme"
            )
            SettingRow(
                checked = dynamicColorScheme,
                onCheckedChange = {
                    if (dynamicColorScheme)
                        coroutineScope.launch { dataStore.setDynamicColorScheme(false) }
                    else coroutineScope.launch { dataStore.setDynamicColorScheme(true) }
                },
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
        Column(modifier = Modifier.fillMaxWidth(.7f)) {
            Text(
                text = title,
                color = if (enabled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = subtitle,
                color = if (enabled) MaterialTheme.colorScheme.secondary.copy(.5f)
                else MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp),
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
fun AnimateColorSchemeChange(content: @Composable () -> Unit) {
    val colors = MaterialTheme.colorScheme.copy(
        background = animateColorAsState(
            targetValue = MaterialTheme.colorScheme.background,
            animationSpec = tween(
                delayMillis = 256,
                durationMillis = 512,
                easing = LinearEasing
            )
        ).value,
        primary = animateColorAsState(
            targetValue = MaterialTheme.colorScheme.primary,
            animationSpec = spring(stiffness = 32f)
        ).value,
    )
    MaterialTheme(colorScheme = colors, content = content)
}