package com.arsildo.prevail.presentation.components.preferences

import androidx.compose.ui.graphics.vector.ImageVector

data class PreferenceCategoryModel(
    val route: String,
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val action: () -> Unit,
)

sealed class PreferenceCategory(val route: String) {
    object Appearance : PreferenceCategory(route = "appearance")
    object Player : PreferenceCategory(route = "player")
}