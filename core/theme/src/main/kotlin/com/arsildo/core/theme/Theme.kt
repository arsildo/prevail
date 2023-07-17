package com.arsildo.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import com.arsildo.core.theme.PrevailUiState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PrevailTheme(
    uiState: PrevailUiState = PrevailUiState(),
    content: @Composable () -> Unit
) {
    val isDarkThemeEnabled =
        if (uiState.isAutomaticThemeEnabled) isSystemInDarkTheme() else uiState.isDarkThemeEnabled
    val systemUiController = rememberSystemUiController()
    DisposableEffect(
        key1 = systemUiController,
        key2 = isDarkThemeEnabled
    ) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = !isDarkThemeEnabled
        )
        onDispose {}
    }
    MaterialTheme(
        colorScheme = if (isDarkThemeEnabled) darkColorScheme() else lightColorScheme(),
        content = content
    )
}