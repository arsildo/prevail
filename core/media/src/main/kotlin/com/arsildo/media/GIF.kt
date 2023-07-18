package com.arsildo.media

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Scale

@Composable
fun BoxScope.GIF(
    url: String,
    context: Context = LocalContext.current
) {
    val model = ImageRequest
        .Builder(context)
        .data(url)
        .scale(Scale.FIT)
        .build()
    val imageLoader = ImageLoader.Builder(context)
        .components {
            @Suppress("MagicNumber")
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    AsyncImage(
        model = model,
        imageLoader = imageLoader,
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxSize()
            .clip(CardDefaults.shape)
    )
}