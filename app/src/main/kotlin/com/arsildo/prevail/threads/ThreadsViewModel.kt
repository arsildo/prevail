package com.arsildo.prevail.threads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.data.ThreadCatalogItem
import com.arsildo.prevail.data.local.ThreadCatalogRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ThreadsScreenUiState(
    val isLoading: Boolean = true,
    val threads: List<ThreadCatalogItem> = emptyList()
)

class ThreadsViewModel(
    private val threadCatalogRepository: ThreadCatalogRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ThreadsScreenUiState())
    val uiState = _uiState.asStateFlow()

    private suspend fun getThreads() = viewModelScope.launch {
        delay(1_000)
        _uiState.update { state ->
            state.copy(
                threads = threadCatalogRepository.getThreadCatalog("po/catalog.json"),
                isLoading = false
            )
        }
    }

    init {
        viewModelScope.launch { getThreads() }
    }

}