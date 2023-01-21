package com.arsildo.prevail.preferences.appearances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.data.source.ColorSchemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AppearancesViewModel @Inject constructor(
    private val repository: ColorSchemeRepository
) : ViewModel() {


    fun getSystemColorScheme() = runBlocking { repository.getSystemColorScheme }

    fun setSystemColorScheme(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { repository.setFollowSystemColorScheme(enabled) }
    }

    fun getColorScheme() = runBlocking { repository.getColorScheme }

    fun setColorScheme(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { repository.setColorScheme(enabled) }
    }

    fun getDynamicColorScheme() = runBlocking { repository.getDynamicColorScheme }

    fun setDynamicColorScheme(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { repository.setDynamicColorScheme(enabled) }
    }

}