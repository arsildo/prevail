package com.arsildo.prevail.logic.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.logic.network.NetworkRepository
import com.arsildo.prevail.logic.network.models.threads.ThreadCatalog
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class MainScreenState {
    object Loading : MainScreenState()
    data class Responded(val data: ThreadCatalog) : MainScreenState()
    data class Failed(val errorMessage: String) : MainScreenState()
}

@HiltViewModel
class ThreadsViewModel @Inject constructor(
    private val repository: NetworkRepository,
    val exoPlayer: ExoPlayer
) : ViewModel() {

    private val _mainScreenState: MutableState<MainScreenState> =
        mutableStateOf(MainScreenState.Loading)
    val mainScreenState: State<MainScreenState> = _mainScreenState

    var threadList: ThreadCatalog = ThreadCatalog()

    init {
        try {
            viewModelScope.launch { requestThreads() }
        } catch (e: Exception) {
            _mainScreenState.value = MainScreenState.Failed("Failed to load.")
        }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }

    fun playVideo(url: String) {
        exoPlayer.apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            playWhenReady = false
            repeatMode = Player.REPEAT_MODE_ALL
        }
    }

    suspend fun requestThreads() {
        _mainScreenState.value = MainScreenState.Loading
        viewModelScope.launch {
            try {
                threadList = repository.getThreadCatalog("wsg/catalog.json")
                delay(512)
                _mainScreenState.value = MainScreenState.Responded(threadList)
            } catch (e: Exception) {
                _mainScreenState.value = MainScreenState.Failed("Failed to load.")
            }
        }
    }

}