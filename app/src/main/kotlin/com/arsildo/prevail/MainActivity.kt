package com.arsildo.prevail

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.core.theme.PrevailTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val viewModel = koinViewModel<MainActivityViewModel>()
            val prevailUiState by viewModel.uiState.collectAsStateWithLifecycle()
            PrevailTheme(
                uiState = prevailUiState,
                content = { PrevailNavigationGraph() }
            )
        }
    }
}
