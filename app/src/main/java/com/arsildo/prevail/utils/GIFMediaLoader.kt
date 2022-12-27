package com.arsildo.prevail.presentation.components.shared

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.arsildo.prevail.di.CURRENT_BOARD
import com.arsildo.prevail.di.MEDIA_BASE_URL

@Composable
fun GIFMediaLoader(gifUri: String) {

    val imageLoader = ImageLoader.Builder(LocalContext.current).components {
        if (SDK_INT >= 28) add(ImageDecoderDecoder.Factory())
        else add(GifDecoder.Factory())
    }.build()

    val imageModel = ImageRequest.Builder(LocalContext.current)
        .data(gifUri)
        .size(Size.ORIGINAL)
        .crossfade(true)
        .crossfade(512)
        .build()

    Box(contentAlignment = Alignment.Center) {
        AsyncImage(
            model = imageModel,
            imageLoader = imageLoader,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clip(MaterialTheme.shapes.small)
                .fillMaxWidth()
                .heightIn((128 + 32).dp)
        )
        Text(
            text = "GIF",
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
