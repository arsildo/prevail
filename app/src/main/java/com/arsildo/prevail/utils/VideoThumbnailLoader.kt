package com.arsildo.prevail.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
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

@Composable
fun VideoThumbnail(
    preloadedThumbnailUri: String,
    videoUri: String,
    playableMedia: @Composable() (BoxScope.() -> Unit)
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
            .precision(Precision.INEXACT)
            .listener(
                onStart = { loadingImage = true },
                onSuccess = { _, _ -> loadingImage = false },
            )
            .build()
    }

    val imageModel = remember {
        ImageRequest.Builder(context)
            .data(videoUri)
            .listener(
                onStart = { loadingImage = true },
                onSuccess = { _, _ -> loadingImage = false },
            )
            .build()
    }



    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clip(MaterialTheme.shapes.large)
            .aspectRatio(LocalMediaAspectRatio.current),
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
            onClick =  {},
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = Color.White.copy(.2f)
            )
        ) {
            Icon(
                Icons.Rounded.PlayArrow,
                contentDescription = null,
                tint = Color.White.copy(.8f),
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

        playableMedia()

    }
}