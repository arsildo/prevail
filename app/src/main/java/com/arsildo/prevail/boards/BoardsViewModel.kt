package com.arsildo.prevail.boards

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.data.models.Board
import com.arsildo.prevail.data.models.Boards
import com.arsildo.prevail.data.source.BoardPreferencesRepository
import com.arsildo.prevail.data.source.ContentRepository
import com.arsildo.prevail.data.source.SavedBoardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.filterList
import javax.inject.Inject


sealed class BoardsScreenState {
    object Loading : BoardsScreenState()
    data class Responded(val data: List<Board>) : BoardsScreenState()
    data class Failed(val errorMessage: String) : BoardsScreenState()
}

@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val repository: ContentRepository,
    private val savedBoardsRepository: SavedBoardsRepository,
    private val boardsPreferencesRepository: BoardPreferencesRepository
) : ViewModel() {

    private val _screenState: MutableState<BoardsScreenState> =
        mutableStateOf(BoardsScreenState.Loading)
    val screenState: State<BoardsScreenState> = _screenState

    private var boardSelection: Boards = Boards(emptyList())
    var boardList: List<Board> = mutableListOf()

    val searchBoard = mutableStateOf("")

    var savedBoards: LiveData<List<Board>> = MutableLiveData(emptyList())

    init {
        try {
            requestBoards()
            getSavedBoards()
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
        boardSelection.boards.forEach { board -> boardList.add(board) }
        return boardList
    }

    fun searchBoards(query: String) {
        searchBoard.value = query
        boardList = transformBoards().filterList {
            title.contains(query, ignoreCase = true) || board.contains(query, ignoreCase = true)
        }
    }

    private fun getSavedBoards() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { savedBoards = savedBoardsRepository.getSavedBoards() }
        }
    }

    fun insertToSavedBoards(board: Board) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { savedBoardsRepository.insertToSavedBoards(board) }
        }
    }

    fun removeFromSavedBoards(board: Board) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { savedBoardsRepository.removeFromSavedBoards(board) }
        }
    }

    fun removeAllBoards() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { savedBoardsRepository.deleteAllSavedBoards() }
        }
    }

}