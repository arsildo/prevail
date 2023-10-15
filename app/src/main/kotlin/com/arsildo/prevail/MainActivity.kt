package com.arsildo.prevail

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.core.theme.MainActivityViewModel
import com.arsildo.core.theme.PrevailTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val viewModel = koinViewModel<MainActivityViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            PrevailTheme(
                darkTheme = if (uiState.isAutomaticThemeEnabled) isSystemInDarkTheme() else uiState.isDarkThemeEnabled,
                dynamicColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) uiState.isDynamicThemeEnabled else false
            ) { PrevailNavigationGraph() }
        }
    }
}
