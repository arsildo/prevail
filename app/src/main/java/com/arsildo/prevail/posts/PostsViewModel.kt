package com.arsildo.prevail.posts

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.data.Post
import com.arsildo.prevail.data.ThreadPosts
import com.arsildo.prevail.data.source.BoardPreferencesRepository
import com.arsildo.prevail.data.source.ContentRepository
import com.arsildo.prevail.data.source.NO_BOARD
import com.arsildo.prevail.data.source.NO_BOARD_DESC
import com.arsildo.prevail.data.source.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class PostsScreenState { Loading, Responded, Failed, }

@HiltViewModel
class PostsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ContentRepository,
    val playerRepository: PlayerRepository,
    private val boardPreferencesRepository: BoardPreferencesRepository
) : ViewModel() {
    private val _screenState: MutableState<PostsScreenState> =
        mutableStateOf(PostsScreenState.Loading)
    val screenState: State<PostsScreenState> = _screenState

    val threadNumber: Int = checkNotNull(savedStateHandle["threadNumber"])

    var postCatalog: ThreadPosts = ThreadPosts(emptyList())
    var postList: List<Post> = emptyList()

    var currentBoard = mutableStateOf(NO_BOARD)
    var currentBoardDesc = mutableStateOf(NO_BOARD_DESC)

    init {
        try {
            requestThread()
        } catch (e: Exception) {
            _screenState.value = PostsScreenState.Failed
        }
    }

    fun requestThread() {
        _screenState.value = PostsScreenState.Loading
        viewModelScope.launch {
            try {
                currentBoard.value =
                    boardPreferencesRepository.getCurrentBoard.stateIn(this).value
                currentBoardDesc.value =
                    boardPreferencesRepository.getCurrentBoardDescription.stateIn(this).value
                postCatalog = repository.getThread(
                    currentThread = currentBoard.value,
                    threadNumber = threadNumber
                )
                postList = transformThreadCatalog()
                delay(1000)
                _screenState.value = PostsScreenState.Responded
            } catch (e: Exception) {
                _screenState.value = PostsScreenState.Failed
            }
        }
    }

    private fun transformThreadCatalog(): List<Post> {
        val postList = mutableListOf<Post>()
        postCatalog.posts.forEach { post -> postList.add(post) }
        return postList
    }

}