package com.arsildo.prevail.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arsildo.prevail.di.CURRENT_BOARD
import com.arsildo.prevail.di.MEDIA_BASE_URL
import com.arsildo.prevail.presentation.components.shared.GIFMediaLoader

@Composable
fun MediaTypeIdentifier(
    mediaType: String,
    mediaHeight: Int,
    mediaWidth: Int,
    mediaID: Long,
) {
    when (mediaType) {
        ".jpg" -> ImageMediaLoader(
            imageUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID.jpg",
            mediaHeight = mediaHeight,
            mediaWidth = mediaWidth
        )
        ".png" -> ImageMediaLoader(
            imageUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID.png",
            mediaHeight = mediaHeight,
            mediaWidth = mediaWidth
        )
        ".gif" -> GIFMediaLoader(
            gifUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID.gif",
            mediaHeight = mediaHeight,
            mediaWidth = mediaWidth
        )
        ".webm" -> VideoThumbnail(
            preloadedThumbnailUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID" + "s.jpg",
            videoUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID.webm",
            mediaHeight = mediaHeight,
            mediaWidth = mediaWidth
        )
        ".pdf" -> Text(text = "PDF")
        else -> Text(text = "Media $mediaType")
    }
}