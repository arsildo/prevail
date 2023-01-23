package com.arsildo.prevail.media

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.prevail.PrevailDestinationsArg.MEDIA_ASPECT_RATIO_ARG
import com.arsildo.prevail.PrevailDestinationsArg.MEDIA_ID_ARG
import com.arsildo.prevail.data.source.BoardPreferencesRepository
import com.arsildo.prevail.data.source.NO_BOARD
import com.arsildo.prevail.data.source.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val playerRepository: PlayerRepository,
    private val boardPreferencesRepository: BoardPreferencesRepository,
) : ViewModel() {

    var currentBoard = mutableStateOf(NO_BOARD)

    val mediaID: Long = checkNotNull(savedStateHandle[MEDIA_ID_ARG])
    val aspectRatio: Float = checkNotNull(savedStateHandle[MEDIA_ASPECT_RATIO_ARG])

    init {
        viewModelScope.launch {
            currentBoard.value = boardPreferencesRepository.getCurrentBoard.stateIn(this).value
        }
    }
}