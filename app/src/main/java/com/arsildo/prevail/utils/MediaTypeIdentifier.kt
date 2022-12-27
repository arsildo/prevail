package com.arsildo.prevail.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arsildo.prevail.di.CURRENT_BOARD
import com.arsildo.prevail.di.MEDIA_BASE_URL
import com.arsildo.prevail.presentation.components.shared.GIFMediaLoader

@Composable
fun MediaTypeIdentifier(
    mediaType: String,
    mediaID: Long,
    onPlayVideoClick: () -> Unit = {},
) {
    when (mediaType) {
        ".jpg" -> ImageMediaLoader(imageUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID.jpg")
        ".png" -> ImageMediaLoader(imageUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID.png")
        ".gif" -> GIFMediaLoader(gifUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID.gif")
        ".pdf" -> Text(text = "PDF")
        ".webm" -> VideoThumbnail(
            preloadedThumbnailUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID" + "s.jpg",
            videoUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID.webm",
            onPlayVideoClick = onPlayVideoClick
        )

        else -> Text(text = "Media $mediaType")
    }
}