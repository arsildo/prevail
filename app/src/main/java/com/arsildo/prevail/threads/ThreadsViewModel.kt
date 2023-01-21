package com.arsildo.prevail.threads

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.data.Board
import com.arsildo.prevail.data.Thread
import com.arsildo.prevail.data.ThreadCatalog
import com.arsildo.prevail.data.source.BoardPreferencesRepository
import com.arsildo.prevail.data.source.ContentRepository
import com.arsildo.prevail.data.source.NO_BOARD
import com.arsildo.prevail.data.source.NO_BOARD_DESC
import com.arsildo.prevail.data.source.PlayerRepository
import com.arsildo.prevail.data.source.SavedBoardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class ThreadsScreenState { Loading, Responded, Failed, EmptyBoards }

@HiltViewModel
class ThreadsViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    val playerRepository: PlayerRepository,
    private val boardPreferencesRepository: BoardPreferencesRepository,
    private val savedBoardsRepository: SavedBoardsRepository,
) : ViewModel() {

    private val _screenState = mutableStateOf(ThreadsScreenState.Loading)
    val screenState: State<ThreadsScreenState> = _screenState

    private var threadCatalog: ThreadCatalog = ThreadCatalog()
    var threadList: List<Thread> = emptyList()

    var currentBoard = mutableStateOf(NO_BOARD)
    var currentBoardDesc = mutableStateOf(NO_BOARD_DESC)
    var savedBoards: LiveData<List<Board>> = MutableLiveData(emptyList())

    init {
        getAllFavoriteBoards()
        viewModelScope.launch {
            getCurrentBoardContext()
            if (currentBoard.value == NO_BOARD)
                _screenState.value = ThreadsScreenState.EmptyBoards
            else try {
                requestThreads()
            } catch (e: Exception) {
                _screenState.value = ThreadsScreenState.Failed
            }
        }
    }

    fun requestThreads() {
        _screenState.value = ThreadsScreenState.Loading
        viewModelScope.launch {
            try {
                getCurrentBoardContext()
                threadCatalog = contentRepository.getThreadCatalog(board = currentBoard.value)
                threadList = flattenThreadsCatalog()
                playerRepository.loadMediaFiles(currentBoard.value, threadList)
                delay(1000)
                _screenState.value = ThreadsScreenState.Responded
            } catch (e: Exception) {
                _screenState.value = ThreadsScreenState.Failed
            }
        }
    }

    fun setLastBoard(board: String, boardDesc: String) = viewModelScope.launch {
        boardPreferencesRepository.setCurrentBoard(board)
        boardPreferencesRepository.setCurrentBoardDescription(boardDesc)
        requestThreads()
    }

    private suspend fun getCurrentBoardContext() {
        currentBoard.value =
            boardPreferencesRepository.getCurrentBoard.stateIn(viewModelScope).value
        currentBoardDesc.value =
            boardPreferencesRepository.getCurrentBoardDescription.stateIn(viewModelScope).value
    }

    private fun flattenThreadsCatalog(): List<Thread> {
        val threadList = mutableListOf<Thread>()
        threadCatalog.forEach { catalogItem ->
            catalogItem.threads.forEach { thread -> threadList.add(thread) }
        }
        return threadList
    }

    private fun getAllFavoriteBoards() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { savedBoards = savedBoardsRepository.getSavedBoards() }
        }
    }

}