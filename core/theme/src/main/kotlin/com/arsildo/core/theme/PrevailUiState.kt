package com.arsildo.core.theme

import androidx.annotation.Keep

@Keep
data class PrevailUiState(
    val isAutomaticThemeEnabled: Boolean = true,
    val isDarkThemeEnabled: Boolean = false,
    val isDynamicThemeEnabled: Boolean = true,
)