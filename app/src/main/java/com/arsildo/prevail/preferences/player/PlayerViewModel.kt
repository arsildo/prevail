package com.arsildo.prevail.preferences.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: PlayerPreferencesRepository
) : ViewModel() {

    fun getAutoPlayMedia() = runBlocking { repository.getAutoPlayMedia }

    fun setAutoPlayMedia(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { repository.setAutoPlayMedia(enabled) }
    }


    fun getPlayMediaMuted() = runBlocking { repository.getPlayMediaMuted }

    fun setPlayMediaMuted(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { repository.setPlayMediaMuted(enabled) }
    }
}