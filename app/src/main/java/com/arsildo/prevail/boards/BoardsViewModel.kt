package com.arsildo.prevail.boards

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.data.Board
import com.arsildo.prevail.data.Boards
import com.arsildo.prevail.data.source.BoardPreferencesRepository
import com.arsildo.prevail.data.source.ContentRepository
import com.arsildo.prevail.data.source.NO_BOARD
import com.arsildo.prevail.data.source.NO_BOARD_DESC
import com.arsildo.prevail.data.source.SavedBoardsRepository
import com.arsildo.prevail.threads.ThreadsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.filterList
import javax.inject.Inject


enum class BoardsScreenState { Loading, Responded, Failed, }

@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val repository: ContentRepository,
    private val savedBoardsRepository: SavedBoardsRepository,
    private val boardPreferencesRepository: BoardPreferencesRepository
) : ViewModel() {

    private val _screenState: MutableState<BoardsScreenState> =
        mutableStateOf(BoardsScreenState.Loading)
    val screenState: State<BoardsScreenState> = _screenState

    private var boardSelection: Boards = Boards(emptyList())
    var boardList: List<Board> = mutableListOf()

    val searchBoard = mutableStateOf("")

    var currentBoard = boardPreferencesRepository.getCurrentBoard
    var savedBoards: LiveData<List<Board>> = MutableLiveData(emptyList())

    init {
        try {
            getSavedBoards()
            requestBoards()
        } catch (e: Exception) {
            _screenState.value = BoardsScreenState.Failed
        }
    }

    fun requestBoards() {
        _screenState.value = BoardsScreenState.Loading
        viewModelScope.launch {
            try {
                boardSelection = repository.getBoards()
                boardList = transformBoards()
                _screenState.value = BoardsScreenState.Responded
            } catch (e: Exception) {
                _screenState.value = BoardsScreenState.Failed
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

    fun removeIfChecked(checked : Boolean, board: Board){
        if (checked) removeFromSavedBoards(board)
        else insertToSavedBoards(board)
    }

    private fun insertToSavedBoards(board: Board) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { savedBoardsRepository.insertToSavedBoards(board) }
        }
    }

    private fun removeFromSavedBoards(board: Board) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                savedBoardsRepository.removeFromSavedBoards(board)
                if (board.board == currentBoard.stateIn(this).value) {
                    boardPreferencesRepository.setCurrentBoard(NO_BOARD)
                    boardPreferencesRepository.setCurrentBoardDescription(NO_BOARD_DESC)
                }
            }
        }
    }

    fun removeAllBoards() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                savedBoardsRepository.deleteAllSavedBoards()
                boardPreferencesRepository.setCurrentBoard(NO_BOARD)
                boardPreferencesRepository.setCurrentBoardDescription(NO_BOARD_DESC)
            }
        }
    }

}