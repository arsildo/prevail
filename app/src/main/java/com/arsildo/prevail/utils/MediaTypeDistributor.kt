package com.arsildo.prevail.utils

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import com.arsildo.prevail.di.MEDIA_BASE_URL
import com.arsildo.prevail.threads.LocalBoardContext

val LocalMediaID = compositionLocalOf { 0L }
val LocalMediaAspectRatio = compositionLocalOf { 1f }

@Composable
fun MediaTypeDistributor(
    mediaType: String,
    mediaID: Long,
    mediaHeight: Int,
    mediaWidth: Int,
    playableMedia: @Composable (BoxScope.() -> Unit)
) {
    val currentBoard = LocalBoardContext.current
    val aspectRatio = remember {
        val ratio = mediaWidth.toFloat() / mediaHeight
        ratio.coerceIn(minimumValue = 0.5f, maximumValue = 2f)
    }
    CompositionLocalProvider(
        LocalMediaID provides mediaID,
        LocalMediaAspectRatio provides aspectRatio
    ) {

        when (mediaType) {
            ".jpg" -> ImageMediaLoader(imageUri = "$MEDIA_BASE_URL$currentBoard/$mediaID.jpg")
            ".png" -> ImageMediaLoader(imageUri = "$MEDIA_BASE_URL$currentBoard/$mediaID.png")
            ".gif" -> GIFMediaLoader(gifUri = "$MEDIA_BASE_URL$currentBoard/$mediaID.gif")
            ".webm" -> {
                VideoThumbnail(
                    preloadedThumbnailUri = "$MEDIA_BASE_URL$currentBoard/${mediaID}s.jpg",
                    videoUri = "$MEDIA_BASE_URL$currentBoard/$mediaID.webm",
                    playableMedia = playableMedia
                )
            }

            else -> Text(text = "Media $mediaType")
        }

    }
}