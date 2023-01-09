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
import com.arsildo.prevail.data.source.ContentRepository
import com.arsildo.prevail.data.source.FavoriteBoardsRepository
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
    private val favoriteBoardsRepository: FavoriteBoardsRepository,
) : ViewModel() {

    private val _screenState: MutableState<BoardsScreenState> =
        mutableStateOf(BoardsScreenState.Loading)
    val screenState: State<BoardsScreenState> = _screenState

    private var boardSelection: Boards = Boards(emptyList())
    var boardList: List<Board> = mutableListOf()

    var savedBoards: LiveData<List<Board>> = MutableLiveData(emptyList())

    init {
        try {
            requestBoards()
            getAllFavoriteBoards()
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
        boardList = transformBoards().filterList {
            title.contains(query, ignoreCase = true) || board.contains(query, ignoreCase = true)
        }
    }

    private fun getAllFavoriteBoards() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { savedBoards = favoriteBoardsRepository.getAllFavoriteBoards() }
        }
    }

    fun insertFavoriteBoard(board: Board) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { favoriteBoardsRepository.insertFavoriteBoard(board) }
        }
    }

    fun removeFavoriteBoard(board: Board) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { favoriteBoardsRepository.removeFavoriteBoard(board) }
        }
    }

    fun removeAllBoards() {
        viewModelScope.launch { withContext(Dispatchers.IO) { favoriteBoardsRepository.deleteSavedBoards() } }
    }

}