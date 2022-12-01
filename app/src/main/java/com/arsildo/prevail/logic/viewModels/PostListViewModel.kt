package com.arsildo.prevail.logic.viewModels

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

sealed class PostListScreenState {
    object Loading : PostListScreenState()
    data class Responded(val data: ThreadPosts) : PostListScreenState()
    data class Failed(val errorMessage: String) : PostListScreenState()
}

@HiltViewModel
class PostListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: NetworkRepository
) : ViewModel() {

    private val _screenState: MutableState<PostListScreenState> =
        mutableStateOf(PostListScreenState.Loading)
    val postListScreenState: State<PostListScreenState> = _screenState

    val threadNumber: Int = checkNotNull(savedStateHandle["threadNumber"])
    val threadName: String = checkNotNull(savedStateHandle["threadName"])

    var postList: ThreadPosts = ThreadPosts(emptyList())

    init {
        try {
            requestThread(threadNumber)
        } catch (e: Exception) {
            _screenState.value = PostListScreenState.Failed("Failed to load.")
        }
    }

    fun requestThread(threadNo: Int) {
        _screenState.value = PostListScreenState.Loading
        viewModelScope.launch {
            try {
                postList = repository.getThread(threadNo)
                delay(1024)
                _screenState.value = PostListScreenState.Responded(postList)
            } catch (e: Exception) {
                _screenState.value = PostListScreenState.Failed("Failed to load.")
            }
        }
    }

}