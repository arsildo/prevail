package com.arsildo.prevail.logic

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.arsildo.prevail.logic.cache.ColorSchemePreferencesKeys
import com.arsildo.prevail.logic.navigation.NavigationGraph
import com.arsildo.prevail.presentation.theme.PrevailTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            val dataStore = ColorSchemePreferencesKeys(LocalContext.current)
            val systemColorScheme = dataStore.getSystemColorScheme.collectAsState(true).value
            val colorScheme = dataStore.getColorScheme.collectAsState(true).value
            val dynamicColorScheme = dataStore.getDynamicColorScheme.collectAsState(true).value

            PrevailTheme(
                darkTheme = if (systemColorScheme) isSystemInDarkTheme() else colorScheme,
                dynamicColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) dynamicColorScheme else false
            ) {
                val navController = rememberNavController()
                NavigationGraph(navController = navController)
            }
        }

    }
}