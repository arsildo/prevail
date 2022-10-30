package com.arsildo.prevail.presentation.screens

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.logic.cache.CachedPreferences
import com.arsildo.prevail.presentation.components.ScreenLayout
import kotlinx.coroutines.launch

@Composable
fun PreferencesScreen(navController: NavController) {

    val dataStore = CachedPreferences(LocalContext.current)
    val themePreference = dataStore.getThemePreference.collectAsState(
        initial = isSystemInDarkTheme()
    ).value
    val dynamicColorSchemePreference = dataStore.getDynamicColorSchemePreference.collectAsState(
        initial = true
    ).value

    val coroutineScope = rememberCoroutineScope()

    AnimateColorSchemeChange {
        ScreenLayout {
            Spacer(modifier = Modifier.height(64.dp))
            SettingRow(
                checked = themePreference,
                onCheckedChange = {
                    if (themePreference)
                        coroutineScope.launch {
                            dataStore.setThemePreference(false)

                        }
                    else coroutineScope.launch { dataStore.setThemePreference(true) }
                },
                enabled = true,
                title = "Follow System Theme",
            )
            SettingRow(
                checked = themePreference,
                onCheckedChange = {
                    if (themePreference)
                        coroutineScope.launch {
                            dataStore.setThemePreference(false)

                        }
                    else coroutineScope.launch { dataStore.setThemePreference(true) }
                },
                enabled = true,
                title = "Dark Theme",
            )
            SettingRow(
                checked = dynamicColorSchemePreference,
                onCheckedChange = {
                    if (dynamicColorSchemePreference)
                        coroutineScope.launch {
                            dataStore.setDynamicColorSchemePreference(false)

                        }
                    else coroutineScope.launch { dataStore.setDynamicColorSchemePreference(true) }
                },
                enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
                title = "Dynamic Theme",
            )
        }
    }
}


@Composable
fun AnimateColorSchemeChange(content: @Composable () -> Unit) {

    val colors = MaterialTheme.colorScheme.copy(
        background = animateColorAsState(
            targetValue = MaterialTheme.colorScheme.background,
            animationSpec = spring(
                stiffness = 32f
            )
        ).value,
        primary = animateColorAsState(
            targetValue = MaterialTheme.colorScheme.primary,
            animationSpec = tween(
                delayMillis = 512,
                durationMillis = 500,
                easing = LinearEasing
            )
        ).value,
    )
    MaterialTheme(colorScheme = colors, content = content)
}


@Composable
fun SettingRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean,
    title: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = title, color = MaterialTheme.colorScheme.primary)
        Switch(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
    }
}