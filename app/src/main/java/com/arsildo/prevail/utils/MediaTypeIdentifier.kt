package com.arsildo.prevail.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arsildo.prevail.data.source.PlayerRepository
import com.arsildo.prevail.di.CURRENT_BOARD
import com.arsildo.prevail.di.MEDIA_BASE_URL

@Composable
fun MediaTypeIdentifier(
    mediaType: String,
    mediaHeight: Int,
    mediaWidth: Int,
    mediaID: Long,
    inFocus: Boolean,
    playerRepository: PlayerRepository,
    onPlayVideoNotInFocus: (Float) -> Unit,
) {

    val aspectRatio = remember {
        val ratio = mediaWidth.toFloat() / mediaHeight
        ratio.coerceIn(minimumValue = 0.5f, maximumValue = 2f)
    }

    when (mediaType) {
        ".jpg" -> ImageMediaLoader(
            imageUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID.jpg",
            aspectRatio = aspectRatio
        )

        ".png" -> ImageMediaLoader(
            imageUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID.png",
            aspectRatio = aspectRatio
        )

        ".gif" -> GIFMediaLoader(
            gifUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID.gif",
            aspectRatio = aspectRatio
        )

        ".webm" -> MediaPlayer(
            mediaID = mediaID,
            aspectRatio = aspectRatio,
            inFocus = inFocus,
            playerRepository = playerRepository,
            onPlayVideoNotInFocus = { onPlayVideoNotInFocus(aspectRatio) }
        )

        ".pdf" -> Text(text = "PDF")
        else -> Text(text = "Media $mediaType")
    }
}