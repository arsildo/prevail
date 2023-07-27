package com.arsildo.prevail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.core.theme.PrevailUiState
import com.arsildo.core.theme.ThemePreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MainActivityViewModel(
    private val themePreferencesRepository: ThemePreferencesRepository
) : ViewModel() {
    val uiState = combine(
        themePreferencesRepository.isAutomaticThemeEnabled,
        themePreferencesRepository.getTheme
    ) { automatic, darkTheme ->
        PrevailUiState(
            isAutomaticThemeEnabled = automatic,
            isDarkThemeEnabled = darkTheme
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PrevailUiState()
    )

    var testValue by mutableStateOf(false)
        private set

    fun updateTestValue() {
        testValue = !testValue
    }

}
