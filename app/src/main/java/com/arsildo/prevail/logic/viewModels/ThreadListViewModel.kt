package com.arsildo.prevail.logic.viewModels

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import com.arsildo.prevail.logic.network.NetworkRepository
import com.arsildo.prevail.logic.network.models.threadList.Thread
import com.arsildo.prevail.logic.network.models.threadList.ThreadCatalog
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ThreadListScreenState {
    object Loading : ThreadListScreenState()
    data class Responded(val data: List<Thread>) : ThreadListScreenState()
    data class Failed(val errorMessage: String) : ThreadListScreenState()
}

@HiltViewModel
class ThreadListViewModel @Inject constructor(
    private val repository: NetworkRepository,
    val exoPlayer: ExoPlayer,
) : ViewModel() {

    private val _screenState: MutableState<ThreadListScreenState> =
        mutableStateOf(ThreadListScreenState.Loading)
    val threadListScreenState: State<ThreadListScreenState> = _screenState

    private var threadCatalog: ThreadCatalog = ThreadCatalog()
    var threadList: List<Thread> = emptyList()

    init {
        try {
            requestThreads()
        } catch (e: Exception) {
            _screenState.value = ThreadListScreenState.Failed("Failed to load.")
        }
    }

    fun requestThreads() {
        _screenState.value = ThreadListScreenState.Loading
        viewModelScope.launch {
            try {
                threadCatalog = repository.getThreadCatalog("sp/catalog.json")
                threadList = transformThreadCatalog()
                delay(1024)
                _screenState.value = ThreadListScreenState.Responded(threadList)
            } catch (e: Exception) {
                _screenState.value = ThreadListScreenState.Failed("Failed to load.")
            }
        }
    }

    private fun transformThreadCatalog(): List<Thread> {
        val threadList = mutableListOf<Thread>()
        threadCatalog.forEach { threadCatalogItem ->
            threadCatalogItem.threads.forEach { thread ->
                threadList.add(thread)
            }
        }
        return threadList
    }

}