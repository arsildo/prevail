package com.arsildo.prevail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.data.local.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

class MainActivityViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    val uiState = combine(
        preferencesRepository.isAutomaticThemeEnabled,
        preferencesRepository.getTheme
    ){ automatic, darkTheme ->
        PrevailUiState(
            isAutomaticThemeEnabled = automatic,
            isDarkThemeEnabled = darkTheme
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PrevailUiState()
    )
}