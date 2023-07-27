package com.arsildo.posts

import ApiError
import ApiException
import ApiSuccess
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.model.Post
import com.arsildo.posts.data.PostsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class PostsUiState(
    val isLoading: Boolean = true,
    val loadingError: String = "",
    val posts: List<Post> = emptyList(),
)

class PostsViewModel(
    private val postsRepository: PostsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostsUiState())
    val uiState = _uiState.asStateFlow()

    private suspend fun getPosts() = viewModelScope.launch {
        _uiState.update { state ->
            when (val response = postsRepository.getThreadCatalog(
                thread = "po",
                threadNumber = checkNotNull(savedStateHandle[THREAD_NUMBER_ARG])
            )) {
                is ApiSuccess -> state.copy(
                    isLoading = false,
                    posts = response.data.posts
                )

                is ApiError -> state.copy(
                    isLoading = false,
                    loadingError = "Failed to connect! (error ${response.code}, ${response.message})"
                )

                is ApiException -> state.copy(
                    isLoading = false,
                    loadingError = "Failed to connect! (${response.e.message})"
                )

                else -> state.copy(isLoading = false)
            }
        }
    }

    init {
        viewModelScope.launch { getPosts() }
    }

}