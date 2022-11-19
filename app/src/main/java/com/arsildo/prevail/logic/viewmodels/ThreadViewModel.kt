package com.arsildo.prevail.logic.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.logic.network.NetworkRepository
import com.arsildo.prevail.logic.network.models.thread.ThreadPosts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ThreadScreenState {
    object Loading : ThreadScreenState()
    data class Responded(val data: ThreadPosts) : ThreadScreenState()
    data class Failed(val errorMessage: String) : ThreadScreenState()
}

@HiltViewModel
class ThreadViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: NetworkRepository
) : ViewModel() {

    private val _threadScreenState: MutableState<ThreadScreenState> =
        mutableStateOf(ThreadScreenState.Loading)
    val threadScreenState: State<ThreadScreenState> = _threadScreenState

    val threadNumber: Int = checkNotNull(savedStateHandle["threadNumber"])
    var postList: ThreadPosts = ThreadPosts(emptyList())


    init {
        try {
            viewModelScope.launch { requestThread(threadNumber) }
        } catch (e: Exception) {
            _threadScreenState.value = ThreadScreenState.Failed("Failed to load.")
        }
    }

    suspend fun requestThread(threadNo: Int) {
        _threadScreenState.value = ThreadScreenState.Loading
        viewModelScope.launch {
            try {
                postList = repository.getThread(threadNo)
                delay(512)
                _threadScreenState.value = ThreadScreenState.Responded(postList)
            } catch (e: Exception) {
                _threadScreenState.value = ThreadScreenState.Failed("Failed to load.")
            }
        }
    }

}