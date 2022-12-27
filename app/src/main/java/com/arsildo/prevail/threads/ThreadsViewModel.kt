package com.arsildo.prevail.threads

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.data.ContentRepository
import com.arsildo.prevail.data.PlayerRepository
import com.arsildo.prevail.data.Thread
import com.arsildo.prevail.data.ThreadCatalog
import com.arsildo.prevail.di.CURRENT_BOARD
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ThreadsScreenState {
    object Loading : ThreadsScreenState()
    data class Responded(val data: List<Thread>) : ThreadsScreenState()
    data class Failed(val errorMessage: String) : ThreadsScreenState()
}

@HiltViewModel
class ThreadsViewModel @Inject constructor(
    private val repository: ContentRepository,
    val player: ExoPlayer,
    private val playerRepository: PlayerRepository,
) : ViewModel() {

    private val _screenState: MutableState<ThreadsScreenState> =
        mutableStateOf(ThreadsScreenState.Loading)
    val threadsScreenState: State<ThreadsScreenState> = _screenState

    private var threadCatalog: ThreadCatalog = ThreadCatalog()
    var threadList: List<Thread> = emptyList()

    init {
        try {
            requestThreads()
        } catch (e: Exception) {
            _screenState.value = ThreadsScreenState.Failed("Failed to load.")
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    fun requestThreads() {
        _screenState.value = ThreadsScreenState.Loading
        viewModelScope.launch {
            try {
                threadCatalog = repository.getThreadCatalog("$CURRENT_BOARD/catalog.json")
                threadList = transformThreadCatalog()
                delay(1000)
                _screenState.value = ThreadsScreenState.Responded(threadList)
            } catch (e: Exception) {
                _screenState.value = ThreadsScreenState.Failed("Failed to load.")
            }
        }
    }

    private fun transformThreadCatalog(): List<Thread> {
        val threadList = mutableListOf<Thread>()
        threadCatalog.forEach { threadCatalogItem ->
            threadCatalogItem.threads.forEach { thread ->
                thread.semantic_url = thread.semantic_url.replace("-", " ")
                threadList.add(thread)
            }
        }
        return threadList
    }

    private fun playerConfiguration() {
    }

    fun playMediaFile(mediaID: Long) = playerRepository.playMediaFile(mediaID)
    fun mutePlayer() = playerRepository.mutePlayer()
    fun unMutePlayer() = playerRepository.unMutePlayer()
    fun clearPlayer() = playerRepository.clearPlayerResources()


}