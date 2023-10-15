package com.arsildo.threadcatalog

import ApiError
import ApiException
import ApiSuccess
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.model.ThreadCatalog
import com.arsildo.threadcatalog.data.ThreadCatalogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ThreadsScreenUiState(
    val isLoading: Boolean = true,
    val loadingError: String = "",
    val threads: List<ThreadCatalog> = emptyList()
)

internal class ThreadCatalogViewModel(
    private val threadCatalogRepository: ThreadCatalogRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ThreadsScreenUiState())
    val uiState = _uiState.asStateFlow()

    private fun getThreads() = viewModelScope.launch {
        _uiState.update { state ->
            when (val response = threadCatalogRepository.getThreadCatalog()) {
                is ApiSuccess -> state.copy(
                    isLoading = false,
                    threads = response.data
                )

                is ApiError -> state.copy(
                    isLoading = false,
                    loadingError = "Failed to connect! (error ${response.code}, ${response.message})"
                )

                is ApiException -> state.copy(
                    isLoading = false,
                    loadingError = "Failed to connect! (${response.e.message})"
                )

                else -> state.copy(isLoading = false)
            }
        }
    }

    init {
        getThreads()
    }

}