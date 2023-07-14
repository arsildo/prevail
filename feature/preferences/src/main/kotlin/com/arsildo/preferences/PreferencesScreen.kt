package com.arsildo.preferences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CancelPresentation
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PreferencesScreen(
    onGeneralClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Preferences") })
        },
        contentWindowInsets = WindowInsets(bottom = 0)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column {
                Preferences(
                    icon = Icons.Rounded.Settings,
                    title = "General",
                    onClick = onGeneralClick
                )
            }
        }
    }
}