package com.arsildo.prevail.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.size.Precision
import coil.size.Size

@Composable
fun VideoThumbnail(
    preloadedThumbnailUri: String,
    videoUri: String,
    onPlayVideoClick: () -> Unit,
) {

    val lowQualityThumbnail = ImageRequest.Builder(LocalContext.current)
        .data(preloadedThumbnailUri)
        .size(Size.ORIGINAL)
        .precision(Precision.INEXACT)
        .crossfade(true)
        .build()

    val thumbnailLoader = ImageLoader.Builder(LocalContext.current)
        .components { add(VideoFrameDecoder.Factory()) }
        .precision(Precision.INEXACT)
        .allowHardware(true)
        .build()

    val imageModel = ImageRequest.Builder(LocalContext.current)
        .data(videoUri)
        .size(Size.ORIGINAL)
        .build()

    Box(
        modifier = Modifier.clip(MaterialTheme.shapes.medium).clickable { onPlayVideoClick() },
        contentAlignment = Alignment.Center
    ) {

        AsyncImage(
            model = lowQualityThumbnail,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            filterQuality = FilterQuality.Low,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .heightIn((128 + 32).dp)
        )

        AsyncImage(
            model = imageModel,
            imageLoader = thumbnailLoader,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            filterQuality = FilterQuality.Medium,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .heightIn((128 + 32).dp)
        )

        IconButton(
            onClick = onPlayVideoClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Black.copy(.2f))
        ) {
            Icon(
                Icons.Rounded.PlayArrow,
                contentDescription = null,
                tint = Color.White,
            )
        }

        Text(
            text = "WEBM",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(Color.Black.copy(.4f))
                .padding(vertical = 2.dp, horizontal = 4.dp)
                .align(Alignment.BottomEnd)
        )
    }
}