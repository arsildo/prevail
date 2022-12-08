package com.arsildo.prevail.logic.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.logic.network.NetworkRepository
import com.arsildo.prevail.logic.network.models.threadPosts.Post
import com.arsildo.prevail.logic.network.models.threadPosts.ThreadPosts
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PostListScreenState {
    object Loading : PostListScreenState()
    data class Responded(val data: List<Post>) : PostListScreenState()
    data class Failed(val errorMessage: String) : PostListScreenState()
}

@HiltViewModel
class PostListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: NetworkRepository,
    val exoPlayer: ExoPlayer,
) : ViewModel() {

    private val _screenState: MutableState<PostListScreenState> =
        mutableStateOf(PostListScreenState.Loading)
    val postListScreenState: State<PostListScreenState> = _screenState

    val threadNumber: Int = checkNotNull(savedStateHandle["threadNumber"])
    val threadName: String = checkNotNull(savedStateHandle["threadName"])

    var postCatalog: ThreadPosts = ThreadPosts(emptyList())
    var postList: List<Post> = emptyList()

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
                postCatalog = repository.getThread(threadNo)
                postList = transformThreadCatalog()
                prepareMediaPlayer()
                delay(1000)
                _screenState.value = PostListScreenState.Responded(postList)
            } catch (e: Exception) {
                _screenState.value = PostListScreenState.Failed("Failed to load.")
            }
        }
    }


    private fun transformThreadCatalog(): List<Post> {
        val postList = mutableListOf<Post>()
        postCatalog.posts.forEach { post -> postList.add(post) }
        return postList
    }

    private fun prepareMediaPlayer() {
        postList.forEachIndexed { index, post ->
            val mediaItem = MediaItem.fromUri("https://i.4cdn.org/sp/${post.tim}.webm")
            exoPlayer.addMediaItem(index, mediaItem)
        }
        exoPlayer.prepare()
        exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ONE
        exoPlayer.playWhenReady = false
    }

}