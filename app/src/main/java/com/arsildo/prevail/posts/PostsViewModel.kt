package com.arsildo.prevail.posts

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.data.ContentRepository
import com.arsildo.prevail.data.Post
import com.arsildo.prevail.data.ThreadPosts
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PostsScreenState {
    object Loading : PostsScreenState()
    data class Responded(val data: List<Post>) : PostsScreenState()
    data class Failed(val errorMessage: String) : PostsScreenState()
}

@HiltViewModel
class PostsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ContentRepository,
    val player: ExoPlayer,
) : ViewModel() {

    private val _screenState: MutableState<PostsScreenState> =
        mutableStateOf(PostsScreenState.Loading)
    val postsScreenState: State<PostsScreenState> = _screenState

    val threadNumber: Int = checkNotNull(savedStateHandle["threadNumber"])

    var postCatalog: ThreadPosts = ThreadPosts(emptyList())
    var postList: List<Post> = emptyList()

    init {
        try {
            requestThread()
            playerConfiguration()
        } catch (e: Exception) {
            _screenState.value = PostsScreenState.Failed("Failed to load.")
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    fun requestThread() {
        _screenState.value = PostsScreenState.Loading
        viewModelScope.launch {
            try {
                postCatalog = repository.getThread(threadNumber)
                postList = transformThreadCatalog()
                delay(1000)
                _screenState.value = PostsScreenState.Responded(postList)
            } catch (e: Exception) {
                _screenState.value = PostsScreenState.Failed("Failed to load.")
            }
        }
    }


    private fun transformThreadCatalog(): List<Post> {
        val postList = mutableListOf<Post>()
        postCatalog.posts.forEach { post -> postList.add(post) }
        return postList
    }

    private fun playerConfiguration() {
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.playWhenReady = false
    }

}