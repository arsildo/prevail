package com.arsildo.prevail.feature.boards

import ApiError
import ApiException
import ApiSuccess
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.model.Board
import com.arsildo.prevail.feature.boards.data.BoardsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class BoardsUiState(
    val isLoading: Boolean = true,
    val loadingError: String = "",
    val boards: List<Board> = emptyList(),
)

class BoardsViewModel(
    private val boardsRepository: BoardsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BoardsUiState())
    val uiState = _uiState.asStateFlow()

    private suspend fun getBoards() = viewModelScope.launch {
        _uiState.update { state ->
            when (val response = boardsRepository.getBoards()) {
                is ApiSuccess -> state.copy(
                    isLoading = false,
                    boards = response.data.boards
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
        viewModelScope.launch { getBoards() }
    }
}