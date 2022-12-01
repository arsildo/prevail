package com.arsildo.prevail.logic.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.logic.network.NetworkRepository
import com.arsildo.prevail.logic.network.models.boards.Board
import com.arsildo.prevail.logic.network.models.boards.Boards
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.filterList
import javax.inject.Inject


sealed class BoardListScreenState {
    object Loading : BoardListScreenState()
    data class Responded(val data: List<Board>) : BoardListScreenState()
    data class Failed(val errorMessage: String) : BoardListScreenState()
}

@HiltViewModel
class BoardListViewModel @Inject constructor(
    private val repository: NetworkRepository
) : ViewModel() {

    private val _screenState: MutableState<BoardListScreenState> =
        mutableStateOf(BoardListScreenState.Loading)
    val boardListScreenState: State<BoardListScreenState> = _screenState

    private var boardSelection: Boards = Boards(emptyList())
    var boardList: List<Board> = mutableListOf()

    init {
        try {
            requestBoards()
        } catch (e: Exception) {
            _screenState.value = BoardListScreenState.Failed("Failed to load.")
        }
    }

    fun requestBoards() {
        _screenState.value = BoardListScreenState.Loading
        viewModelScope.launch {
            try {
                boardSelection = repository.getBoards()
                boardList = transformBoards()
                delay(1024)
                _screenState.value = BoardListScreenState.Responded(boardList)
            } catch (e: Exception) {
                _screenState.value = BoardListScreenState.Failed("Failed to load.")
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