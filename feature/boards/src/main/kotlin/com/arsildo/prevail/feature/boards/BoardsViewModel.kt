package com.arsildo.prevail.feature.boards

import ApiError
import ApiException
import ApiSuccess
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.util.fastFilter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.model.Board
import com.arsildo.prevail.feature.boards.data.BoardsRepository
import com.arsildo.prevail.feature.boards.data.LastVisitedBoardRepository
import com.arsildo.utils.Formaters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.cache
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.filterList


data class BoardsUiState(
    val isLoading: Boolean = true,
    val loadingError: String = "",
    val boards: List<Board> = emptyList(),
    val searchQuery: String = "",
    val favoriteBoard: String = ""
)

internal class BoardsViewModel(
    private val boardsRepository: BoardsRepository,
    private val lastVisitedBoardRepository: LastVisitedBoardRepository,
) : ViewModel() {

    // saves a copy of all boards
    private var boardsResponse = emptyList<Board>()

    private val _uiState = MutableStateFlow(BoardsUiState())
    val uiState = combine(
        _uiState,
        lastVisitedBoardRepository.getLastVisitedBoard
    ) { state, favoriteBoard ->
        BoardsUiState(
            isLoading = state.isLoading,
            loadingError = state.loadingError,
            boards = state.boards,
            searchQuery = state.searchQuery,
            favoriteBoard = favoriteBoard
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = BoardsUiState(),
        started = SharingStarted.WhileSubscribed(5000)
    )

    private fun getBoards() = viewModelScope.launch {
        _uiState.update { state ->
            when (val response = boardsRepository.getBoards()) {
                is ApiSuccess -> {

                    state.copy(
                        isLoading = false,
                        boards = response.data.boards
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
            boards = uiState.value.boards.filter { it.meta_description.contains(query) }
        )
    }

    fun clearSearchQuery() = _uiState.update { state ->
        state.copy(
            searchQuery = "",
            boards = boardsResponse
        )
    }

    fun setFavoriteBoard(board: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            lastVisitedBoardRepository.setLastVisitedBoard(board = board)
        }
    }
}