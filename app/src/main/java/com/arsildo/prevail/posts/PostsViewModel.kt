package com.arsildo.prevail.posts

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.PrevailDestinationsArg.THREAD_NUMBER_ARG
import com.arsildo.prevail.data.Post
import com.arsildo.prevail.data.ThreadPosts
import com.arsildo.prevail.data.source.BoardPreferencesRepository
import com.arsildo.prevail.data.source.ContentRepository
import com.arsildo.prevail.data.source.NO_BOARD
import com.arsildo.prevail.data.source.PlayerRepository
import com.arsildo.prevail.di.BASE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class PostsScreenState { Loading, Responded, Failed }

@HiltViewModel
class PostsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ContentRepository,
    val playerRepository: PlayerRepository,
    private val boardPreferencesRepository: BoardPreferencesRepository
) : ViewModel() {
    private val _screenState = mutableStateOf(PostsScreenState.Loading)
    val screenState: State<PostsScreenState> = _screenState

    val threadNumber: Int = checkNotNull(savedStateHandle[THREAD_NUMBER_ARG])

    var postCatalog: ThreadPosts = ThreadPosts(emptyList())
    var postList: List<Post> = emptyList()

    var currentBoard = mutableStateOf(NO_BOARD)

    init {
        try {
            requestThread()
        } catch (e: Exception) {
            _screenState.value = PostsScreenState.Failed
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerRepository.player?.release()
    }

    fun requestThread() {
        _screenState.value = PostsScreenState.Loading
        viewModelScope.launch {
            try {
                getCurrentBoardContext()
                postCatalog = repository.getThread(
                    currentThread = currentBoard.value,
                    threadNumber = threadNumber
                )
                playerRepository.loadPostsMediaFiles(currentBoard.value,postList)
                postList = flattenPostList()
                _screenState.value = PostsScreenState.Responded
            } catch (e: Exception) {
                _screenState.value = PostsScreenState.Failed
            }
        }
    }

    private fun flattenPostList(): List<Post> {
        val postList = mutableListOf<Post>()
        postCatalog.posts.forEach { post -> postList.add(post) }
        return postList
    }

    private suspend fun getCurrentBoardContext() {
        currentBoard.value =
            boardPreferencesRepository.getCurrentBoard.stateIn(viewModelScope).value
    }

    fun openThreadInBrowser(uriHandler: UriHandler) {
        uriHandler.openUri("$BASE_URL${currentBoard.value}/thread/$threadNumber")
    }

}