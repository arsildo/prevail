package com.arsildo.prevail.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import com.arsildo.prevail.data.source.PlayerRepository
import com.arsildo.prevail.di.MEDIA_BASE_URL
import com.arsildo.prevail.threads.LocalBoardContext


val LocalMediaAspectRatio = compositionLocalOf { 1f }

@Composable
fun MediaTypeIdentifier(
    mediaType: String,
    mediaHeight: Int,
    mediaWidth: Int,
    mediaID: Long,
    inFocus: Boolean,
    playerRepository: PlayerRepository,
    onMediaScreenClick: (Float) -> Unit,
) {
    val currentBoard = LocalBoardContext.current
    val aspectRatio = remember {
        val ratio = mediaWidth.toFloat() / mediaHeight
        ratio.coerceIn(minimumValue = 0.5f, maximumValue = 2f)
    }
    CompositionLocalProvider(LocalMediaAspectRatio provides aspectRatio) {
        when (mediaType) {
            ".jpg" -> ImageMediaLoader(imageUri = "$MEDIA_BASE_URL$currentBoard/$mediaID.jpg")
            ".png" -> ImageMediaLoader(imageUri = "$MEDIA_BASE_URL$currentBoard/$mediaID.png")
            ".gif" -> GIFMediaLoader(gifUri = "$MEDIA_BASE_URL$currentBoard/$mediaID.gif")
            ".webm" -> MediaPlayer(
                mediaID = mediaID,
                inFocus = inFocus,
                playerRepository = playerRepository,
                onMediaScreenClick = { aspectRatio -> onMediaScreenClick(aspectRatio) }
            )

            ".pdf" -> Text(text = "PDF")
            else -> Text(text = "Media $mediaType")
        }
    }
}