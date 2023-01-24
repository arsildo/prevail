package com.arsildo.prevail.preferences.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class GeneralsViewModel @Inject constructor(
    private val repository: GeneralsRepository
) : ViewModel() {

    fun getConfirmAppExit() = runBlocking { repository.getConfirmAppExit }

    fun setConfirmAppExit(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { repository.setConfirmAppExit(enabled) }
    }

}