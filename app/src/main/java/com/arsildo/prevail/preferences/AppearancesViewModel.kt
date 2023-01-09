package com.arsildo.prevail.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.data.source.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AppearancesViewModel @Inject constructor(private val repository: DataStoreRepository) : ViewModel() {


    fun getSystemColorScheme() = runBlocking { repository.getSystemColorScheme }

    fun setSystemColorScheme(enabled: Boolean) {
        viewModelScope.launch {
            repository.setFollowSystemColorScheme(enabled)
        }
    }

    fun getColorScheme() = runBlocking { repository.getColorScheme }

    fun setColorScheme(enabled: Boolean) {
        viewModelScope.launch {
            repository.setColorScheme(enabled)
        }
    }

    fun getDynamicColorScheme() = runBlocking { repository.getDynamicColorScheme }

    fun setDynamicColorScheme(enabled: Boolean) {
        viewModelScope.launch {
            repository.setDynamicColorScheme(enabled)
        }
    }

}