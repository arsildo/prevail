package com.arsildo.preferences.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.core.theme.ThemePreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AppearancePreferencesUiState(
    val isAutomaticThemeEnabled: Boolean = true,
    val isDarkThemeEnabled: Boolean = false
)

class AppearancePreferencesViewModel(
    private val themePreferencesRepository: ThemePreferencesRepository,
) : ViewModel() {
    val uiState = combine(
        themePreferencesRepository.isAutomaticThemeEnabled,
        themePreferencesRepository.getTheme,
    ) { isAutomaticThemeEnabled, isDarkThemeEnabled ->
        AppearancePreferencesUiState(
            isAutomaticThemeEnabled = isAutomaticThemeEnabled,
            isDarkThemeEnabled = isDarkThemeEnabled
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppearancePreferencesUiState()
    )

    fun setAutomaticTheme(enabled: Boolean) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            themePreferencesRepository.setAutomaticTheme(enabled)
        }
    }
}