package com.arsildo.threadcatalog

import ApiError
import ApiException
import ApiSuccess
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.model.ThreadCatalog
import com.arsildo.prevail.feature.boards.data.LastVisitedBoardRepository
import com.arsildo.threadcatalog.data.ThreadCatalogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ThreadsScreenUiState(
    val isLoading: Boolean = true,
    val loadingError: String = "",
    val lastVisitedBoard: String = "",
    val threads: List<ThreadCatalog> = emptyList()
)

internal class ThreadCatalogViewModel(
    private val threadCatalogRepository: ThreadCatalogRepository,
    private val lastVisitedBoardRepository: LastVisitedBoardRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ThreadsScreenUiState())
    val uiState = combine(
        _uiState,
        lastVisitedBoardRepository.getLastVisitedBoard
    ) { uiState, lastVisitedBoard ->
        ThreadsScreenUiState(
            isLoading = uiState.isLoading,
            loadingError = uiState.loadingError,
            lastVisitedBoard = lastVisitedBoard,
            threads = uiState.threads
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = ThreadsScreenUiState(),
        started = SharingStarted.WhileSubscribed(5_000)
    )

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