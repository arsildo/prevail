package com.arsildo.prevail.presentation.screens.preferences

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.logic.cache.ColorSchemePreferencesKeys
import com.arsildo.prevail.logic.cache.MediaPlayerPreferenceKeys

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferenceDetailScreen(
    category: String?,
    navController: NavController,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState,
        snapAnimationSpec = tween(delayMillis = 0, easing = LinearOutSlowInEasing)
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        contentColor = MaterialTheme.colorScheme.onBackground,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            when (category) {

                "appearances" -> {
                    Column {
                        PreferenceCategoryLabel(title = "Appearance", navController = navController)
                        val colorSchemePreferences =
                            ColorSchemePreferencesKeys(LocalContext.current)
                        AppearancesScreen(dataStore = colorSchemePreferences)
                    }
                }

                "mediaPlayer" -> {
                    Column {
                        PreferenceCategoryLabel(
                            title = "Media Player",
                            navController = navController
                        )
                        val mediaPlayerPreferences = MediaPlayerPreferenceKeys(LocalContext.current)
                        MediaPlayerScreen(dataStore = mediaPlayerPreferences)
                    }
                }

                else -> Text(text = "Category not found!")
            }

        }
    }

}


@Composable
fun PreferenceCategoryLabel(title: String, navController: NavController) {
    Row(
        modifier = Modifier.statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                Icons.Rounded.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
    }
}