package com.arsildo.prevail.presentation.components.shared

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MediaController(
    mediaType: String,
    mediaID: Long,
) {
    when (mediaType) {
        ".jpg" -> ImageMedia(mediaID, ".jpg")
        ".png" -> ImageMedia(mediaID, ".png")
        ".gif" -> GIFMedia(id = mediaID)
        ".pdf" -> Text(text = "PDF Type")
        ".webm" -> Text(text = "WEBM Type")
        else -> Text(text = "Media $mediaType")
    }
}