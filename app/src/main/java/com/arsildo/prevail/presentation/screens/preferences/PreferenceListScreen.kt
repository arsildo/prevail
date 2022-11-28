package com.arsildo.prevail.presentation.screens.preferences

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.logic.constants.APPLICATION_VERSION
import com.arsildo.prevail.logic.navigation.ContentRoute
import com.arsildo.prevail.presentation.components.shared.AppBar

data class PreferenceCategoryModel(
    val route: String,
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val action: () -> Unit,
)

sealed class PreferenceCategoryRoute(val route: String) {
    object Appearances : PreferenceCategoryRoute(route = "appearances")
    object MediaPlayer : PreferenceCategoryRoute(route = "mediaPlayer")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferenceListScreen(
    navController: NavController,
    onPreferenceCategoryClicked: (String) -> Unit
) {

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState,
        snapAnimationSpec = tween(delayMillis = 0, easing = LinearOutSlowInEasing)
    )

    val statusBarPadding by animateDpAsState(
        if (topAppBarState.collapsedFraction < .99)
            WindowInsets.statusBars.asPaddingValues().calculateTopPadding().value.dp else 0.dp,
        animationSpec = tween(delayMillis = 0, easing = LinearOutSlowInEasing)
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            AppBar(
                title = { Text("Preferences") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ContentRoute.ThreadList.route) }) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                statusBarPadding = statusBarPadding
            )
        },
        contentColor = MaterialTheme.colorScheme.onBackground,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            val preferenceList = listOf(
                PreferenceCategoryModel(
                    route = PreferenceCategoryRoute.Appearances.route,
                    icon = Icons.Outlined.Palette,
                    title = "Appearance",
                    subtitle = "Customize the look of your experience.",
                    action = { onPreferenceCategoryClicked(PreferenceCategoryRoute.Appearances.route) }
                ),
                PreferenceCategoryModel(
                    route = PreferenceCategoryRoute.MediaPlayer.route,
                    icon = Icons.Rounded.PlayArrow,
                    title = "Media Player",
                    subtitle = "Customize the media player behaviour.",
                    action = { onPreferenceCategoryClicked(PreferenceCategoryRoute.MediaPlayer.route) }
                ),
                PreferenceCategoryModel(
                    route = "about",
                    icon = Icons.Outlined.Info,
                    title = "About",
                    subtitle = "Version $APPLICATION_VERSION",
                    action = {}
                )

            )
            LazyColumn {
                items(preferenceList.size) { PreferenceCategory(preference = preferenceList[it]) }
            }
        }
    }


}

@Composable
fun PreferenceCategory(preference: PreferenceCategoryModel) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { preference.action() }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                preference.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = preference.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = preference.subtitle,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}