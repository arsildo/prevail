package com.arsildo.prevail.logic.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.logic.network.NetworkRepository
import com.arsildo.prevail.logic.network.models.threads.ThreadCatalogItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class MainScreenState {
    object Loading : MainScreenState()
    data class Responded(val data: List<ThreadCatalogItem>) : MainScreenState()
    data class Failed(val errorMessage: String) : MainScreenState()
}


@HiltViewModel
class ThreadsViewModel @Inject constructor(
    private val repository: NetworkRepository
) : ViewModel() {

    private val _mainScreenState: MutableState<MainScreenState> = mutableStateOf(MainScreenState.Loading)
    val mainScreenState: State<MainScreenState> = _mainScreenState

    var threadList: MutableState<List<ThreadCatalogItem>> = mutableStateOf(ArrayList())

    init {
        try {
            _mainScreenState.value = MainScreenState.Loading
            viewModelScope.launch {
                try {
                    threadList.value = repository.getThreadCatalog("wsg/catalog.json")
                    _mainScreenState.value = MainScreenState.Responded(threadList.value)
                } catch (e: Exception) {
                    _mainScreenState.value = MainScreenState.Failed("Failed to load.")
                }
            }
        } catch (e: Exception) {
            _mainScreenState.value = MainScreenState.Failed("Failed to load.")
        }
    }
}