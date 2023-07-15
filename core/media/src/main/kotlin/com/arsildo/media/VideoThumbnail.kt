package com.arsildo.media

import android.content.Context
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.size.Scale

@Composable
fun BoxScope.VideoThumbnail(
    url: String,
    context: Context = LocalContext.current
) {
    val model = ImageRequest
        .Builder(context)
        .data(url)
        .scale(Scale.FIT)
        .build()
    val thumbnailLoader = ImageLoader
        .Builder(context)
        .components { add(VideoFrameDecoder.Factory()) }
        .build()
    AsyncImage(
        model = model,
        imageLoader = thumbnailLoader,
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxSize()
            .clip(CardDefaults.shape)
    )
}