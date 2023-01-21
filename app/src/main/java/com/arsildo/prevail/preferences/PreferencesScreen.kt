package com.arsildo.prevail.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PlayArrow
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.ContentScreens
import com.arsildo.prevail.PrevailDestinations
import com.arsildo.prevail.utils.PrevailAppBar

data class PreferenceCategoryModel(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val action: () -> Unit,
)

fun providePreferenceList(navController: NavController) = listOf(
    PreferenceCategoryModel(
        title = "Appearance",
        subtitle = "Customize the look of your experience.",
        icon = Icons.Outlined.Palette,
        action = { navController.navigate(PrevailDestinations.APPEARANCE_PREFS_ROUTE) }
    ),
    PreferenceCategoryModel(
        title = "Player",
        subtitle = "Customize how the video player behaves.",
        icon = Icons.Outlined.PlayArrow,
        action = { navController.navigate(PrevailDestinations.PLAYER_PREFS_ROUTE) }
    ),
    PreferenceCategoryModel(
        title = "About",
        subtitle = "Version 1.0",
        icon = Icons.Outlined.Info,
        action = {}
    )

)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(navController: NavController) {


    val preferenceList = providePreferenceList(navController)

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = topAppBarState)

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            PrevailAppBar(
                title = { Text("Preferences") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ContentScreens.THREADS_SCREEN) }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                scrollBehavior = scrollBehavior,
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
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = preferenceList) { preference ->
                    PreferenceCategoryItem(preference = preference, onClick = preference.action)
                }
            }
        }
    }


}

@Composable
fun PreferenceCategoryItem(
    preference: PreferenceCategoryModel,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                preference.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
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