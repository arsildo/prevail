package com.arsildo.prevail.media

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.arsildo.prevail.PrevailDestinationsArg.MEDIA_INDEX_ARG
import com.arsildo.prevail.data.source.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val mediaID: Int = checkNotNull(savedStateHandle[MEDIA_INDEX_ARG])

}