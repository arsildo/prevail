package com.arsildo.prevail.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
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
    mediaWidth: Int,
    onImageClick: () -> Unit = {},
    onImageLongClick: () -> Unit = {},
) {

    var loadingImage by remember { mutableStateOf(false) }
    var failedToLoad by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val imageModel = remember {
        ImageRequest.Builder(context)
            .data(imageUri)
            .listener(
                onStart = { loadingImage = true },
                onSuccess = { _, _ -> loadingImage = false },
                onError = { _, _ -> failedToLoad = true }
            )
            .size(width = mediaWidth, height = mediaHeight)
            .scale(Scale.FIT)
            .crossfade(true)
            .crossfade(512)
            .build()
    }

    val aspectRatio = remember {
        val ratio = mediaWidth.toFloat() / mediaHeight
        ratio.coerceIn(minimumValue = 0.5f, maximumValue = 2f)
    }

    Box(contentAlignment = Alignment.Center) {
        AsyncImage(
            model = imageModel,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clip(MaterialTheme.shapes.small)
                .aspectRatio(aspectRatio)
                .fillMaxSize()
                .placeholder(
                    visible = loadingImage,
                    highlight = PlaceholderHighlight.fade(highlightColor = MaterialTheme.colorScheme.primaryContainer),
                    color = MaterialTheme.colorScheme.surfaceVariant,
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