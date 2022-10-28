package com.arsildo.prevail.presentation.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dangerous
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.arsildo.prevail.logic.cache.CachedPreferences
import com.arsildo.prevail.presentation.components.ScreenLayout

@Composable
fun PreferencesScreen(navController: NavController) {

    val dataStore = CachedPreferences(LocalContext.current)
    val dynamicColorSchemePreference = dataStore.getDynamicColorSchemePreference.collectAsState(
        initial = true
    ).value

    val coroutineScope = rememberCoroutineScope()

    ScreenLayout {


        SettingRow(icon = Icons.Rounded.Dangerous, title = "Danger")
        SettingRow(icon = Icons.Rounded.Place, title = "Hello")
    }
}


@Composable
fun SettingRow(
    icon: ImageVector,
    title: String,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(text = title, color = MaterialTheme.colorScheme.primary)
    }
}