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
import org.koin.core.context.startKoin


data class BoardsUiState(
    val isLoading: Boolean = true,
    val loadingError: String = "",
    val boards: List<Board> = emptyList(),
    val searchQuery: String = "",
)

class BoardsViewModel(
    private val boardsRepository: BoardsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BoardsUiState())
    val uiState = _uiState.asStateFlow()

    // saves a copy of all boards
    private var boardsResponse = emptyList<Board>()

    private fun getBoards() = viewModelScope.launch {
        _uiState.update { state ->
            when (val response = boardsRepository.getBoards()) {
                is ApiSuccess -> {
                    boardsResponse = response.data.boards
                    state.copy(
                        isLoading = false,
                        boards = boardsResponse
                    )
                }

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
        getBoards()
    }

    fun updateSearchQuery(query: String) = _uiState.update { state ->
        state.copy(
            searchQuery = query,
            boards = boardsResponse.filter { board ->
                board.board.contains(query, ignoreCase = true) ||
                        board.meta_description?.contains(query, ignoreCase = true) == true
            }
        )
    }

    fun clearSearchQuery() = _uiState.update { state ->
        state.copy(
            searchQuery = "",
            boards = boardsResponse
        )
    }

}