package com.arsildo.prevail.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.placeholder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageMediaLoader(
    imageUri: String,
    mediaHeight: Int,
    onImageClick: () -> Unit = {},
    onImageLongClick: () -> Unit = {},
) {

    var loadingImage by remember { mutableStateOf(false) }
    var failedToLoad by remember { mutableStateOf(false) }

    val imageModel = ImageRequest.Builder(LocalContext.current)
        .data(imageUri)
        .listener(
            onStart = { loadingImage = true },
            onError = { _, _ -> failedToLoad = true },
            onSuccess = { _, _ -> loadingImage = false }
        )
        .crossfade(true)
        .crossfade(512)
        .build()

    val mediaH = with(LocalDensity.current) { mediaHeight.toDp() }
    Box(
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageModel,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .requiredHeightIn(mediaH)
                .fillMaxWidth()
                .placeholder(
                    visible = loadingImage,
                    highlight = PlaceholderHighlight.fade(highlightColor = MaterialTheme.colorScheme.primary),
                    color = MaterialTheme.colorScheme.background,
                )
                .combinedClickable(
                    onClick = onImageClick,
                    onLongClick = onImageLongClick
                )
        )
        if (failedToLoad)
            Text(
                text = "Failed to load image.",
                color = MaterialTheme.colorScheme.primary
            )
    }

}