package com.arsildo.prevail.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
    onPlayVideoNotInFocus: () -> Unit,
) {
    val context = LocalContext.current

    var loadingImage by remember { mutableStateOf(false) }

    val thumbnailLoader = remember {
        ImageLoader.Builder(context)
            .components { add(VideoFrameDecoder.Factory()) }
            .precision(Precision.INEXACT)
            .build()
    }


    val lowQualityThumbnail = remember {
        ImageRequest.Builder(context)
            .data(preloadedThumbnailUri)
            .size(Size.ORIGINAL)
            .listener(
                onStart = { loadingImage = true },
                onSuccess = { _, _ -> loadingImage = false },
            )
            .build()
    }

    val imageModel = remember {
        ImageRequest.Builder(context)
            .data(videoUri)
            .size(Size.ORIGINAL)
            .listener(
                onStart = { loadingImage = true },
                onSuccess = { _, _ -> loadingImage = false },
            )
            .build()
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onPlayVideoNotInFocus),
        contentAlignment = Alignment.Center
    ) {

        AsyncImage(
            model = lowQualityThumbnail,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            filterQuality = FilterQuality.Medium,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .fillMaxWidth()
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
        )


        IconButton(
            onClick = onPlayVideoNotInFocus,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(.2f)
            )
        ) {
            Icon(
                Icons.Rounded.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary.copy(.8f),
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