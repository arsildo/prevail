package com.arsildo.prevail.logic.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.logic.network.NetworkRepository
import com.arsildo.prevail.logic.network.models.boards.Boards
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class BoardsScreenState {
    object Loading : BoardsScreenState()
    data class Responded(val data: Boards) : BoardsScreenState()
    data class Failed(val errorMessage: String) : BoardsScreenState()
}

@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val repository: NetworkRepository
) : ViewModel() {

    private val _boardsScreenState: MutableState<BoardsScreenState> =
        mutableStateOf(BoardsScreenState.Loading)
    val boardsScreenState: State<BoardsScreenState> = _boardsScreenState

    val boardList: MutableState<Boards> = mutableStateOf(Boards(emptyList()))

    init {
        try {
            viewModelScope.launch { requestBoards() }
        } catch (e: Exception) {
            _boardsScreenState.value = BoardsScreenState.Failed("Failed to load.")
        }
    }

    suspend fun requestBoards() {
        _boardsScreenState.value = BoardsScreenState.Loading
        viewModelScope.launch {
            try {
                boardList.value = repository.getBoards()
                delay(512)
                _boardsScreenState.value = BoardsScreenState.Responded(boardList.value)
            } catch (e: Exception) {
                _boardsScreenState.value = BoardsScreenState.Failed("Failed to load.")
            }
        }
    }
}