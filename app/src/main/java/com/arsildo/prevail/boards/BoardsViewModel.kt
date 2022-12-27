package com.arsildo.prevail.boards

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.data.Board
import com.arsildo.prevail.data.Boards
import com.arsildo.prevail.data.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.filterList
import javax.inject.Inject


sealed class BoardsScreenState {
    object Loading : BoardsScreenState()
    data class Responded(val data: List<Board>) : BoardsScreenState()
    data class Failed(val errorMessage: String) : BoardsScreenState()
}

@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    private val _screenState: MutableState<BoardsScreenState> =
        mutableStateOf(BoardsScreenState.Loading)
    val boardsScreenState: State<BoardsScreenState> = _screenState

    private var boardSelection: Boards = Boards(emptyList())
    var boardList: List<Board> = mutableListOf()

    init {
        try {
            requestBoards()
        } catch (e: Exception) {
            _screenState.value = BoardsScreenState.Failed("Failed to load.")
        }
    }

    fun requestBoards() {
        _screenState.value = BoardsScreenState.Loading
        viewModelScope.launch {
            try {
                boardSelection = repository.getBoards()
                boardList = transformBoards()
                _screenState.value = BoardsScreenState.Responded(boardList)
            } catch (e: Exception) {
                _screenState.value = BoardsScreenState.Failed("Failed to load.")
            }
        }
    }

    private fun transformBoards(): List<Board> {
        val boardList = mutableListOf<Board>()
        boardSelection.boards.forEach { board ->
            boardList.add(board)
        }
        return boardList
    }

    fun searchBoards(query: String) {
        boardList = transformBoards().filterList {
            title.contains(query, ignoreCase = true) || board.contains(query, ignoreCase = true)
        }
    }
}