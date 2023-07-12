package com.arsildo.prevail

import androidx.compose.runtime.Stable

@Stable
data class PrevailUiState(
    val isAutomaticThemeEnabled : Boolean = true,
    val isDarkThemeEnabled : Boolean = false,
)