package com.arsildo.prevail.threads

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.data.models.Board
import com.arsildo.prevail.data.models.Thread
import com.arsildo.prevail.data.models.ThreadCatalog
import com.arsildo.prevail.data.source.BoardPreferencesRepository
import com.arsildo.prevail.data.source.ContentRepository
import com.arsildo.prevail.data.source.PlayerRepository
import com.arsildo.prevail.data.source.SavedBoardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


sealed class ThreadsScreenState {
    object Loading : ThreadsScreenState()
    data class Responded(val data: List<Thread>) : ThreadsScreenState()
    data class Failed(val errorMessage: String) : ThreadsScreenState()
}

@HiltViewModel
class ThreadsViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    val playerRepository: PlayerRepository,
    private val savedBoardsRepository: SavedBoardsRepository,
    val boardPreferencesRepository: BoardPreferencesRepository,
) : ViewModel() {

    private val _screenState: MutableState<ThreadsScreenState> =
        mutableStateOf(ThreadsScreenState.Loading)
    val screenState: State<ThreadsScreenState> = _screenState

    private var threadCatalog: ThreadCatalog = ThreadCatalog()
    var threadList: List<Thread> = emptyList()

    var savedBoards: LiveData<List<Board>> = MutableLiveData(emptyList())

    init {
        try {
            getAllFavoriteBoards()
            requestThreads()
        } catch (e: Exception) {
            _screenState.value = ThreadsScreenState.Failed("Failed to load.")
        }
    }

    fun requestThreads() {
        _screenState.value = ThreadsScreenState.Loading
        getSelectedBoard()
        viewModelScope.launch {
            try {
                threadCatalog =
                    contentRepository.getThreadCatalog(boardPreferencesRepository.currentBoard.value)
                threadList = transformThreadCatalog()
                delay(1000)
                _screenState.value = ThreadsScreenState.Responded(threadList)
            } catch (e: Exception) {
                _screenState.value = ThreadsScreenState.Failed("Failed to load.")
            }
        }
    }


    private fun getSelectedBoard() = viewModelScope.launch {
        boardPreferencesRepository.currentBoard.value =
            boardPreferencesRepository.getLastBoard.stateIn(this).value
        boardPreferencesRepository.currentBoardDesc.value =
            boardPreferencesRepository.getLastBoardDescription.stateIn(this).value
    }


    fun setLastBoard(board: String, boardDesc: String) = viewModelScope.launch {
        boardPreferencesRepository.setLastBoard(board)
        boardPreferencesRepository.setLastBoardDescription(boardDesc)
    }


    private fun transformThreadCatalog(): List<Thread> {
        val threadList = mutableListOf<Thread>()
        threadCatalog.forEach { threadCatalogItem ->
            threadCatalogItem.threads.forEach { thread -> threadList.add(thread) }
        }
        return threadList
    }

    private fun getAllFavoriteBoards() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                savedBoards = savedBoardsRepository.getSavedBoards()
            }
        }
    }

}