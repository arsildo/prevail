package com.arsildo.prevail.logic.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.logic.network.model.Board
import com.arsildo.prevail.logic.network.model.BoardList
import com.arsildo.prevail.logic.repository.BoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ScreenState {
    object Loading : ScreenState()
    data class Responded(val data: BoardList) : ScreenState()
    data class Failed(val errorMessage: String) : ScreenState()
}


@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val repository: BoardRepository
) : ViewModel() {

    private val _screenState: MutableState<ScreenState> = mutableStateOf(ScreenState.Loading)
    val screenState: State<ScreenState> = _screenState

    val boardList : MutableState<BoardList?> = mutableStateOf(null)

    init {
        try {
            _screenState.value = ScreenState.Loading
            viewModelScope.launch {
                try {
                    boardList.value = repository.getBoards()
                    _screenState.value = ScreenState.Responded(data = boardList.value!!)
                } catch (e: Exception) {
                    _screenState.value = ScreenState.Failed("Failed to load.")
                }
            }
        } catch (e: Exception) {
            _screenState.value = ScreenState.Failed("Failed to load.")
        }
    }
}