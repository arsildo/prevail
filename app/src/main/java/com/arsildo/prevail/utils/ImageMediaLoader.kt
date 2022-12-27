package com.arsildo.prevail.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.arsildo.prevail.di.CURRENT_BOARD
import com.arsildo.prevail.di.MEDIA_BASE_URL

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageMediaLoader(
    imageUri: String,
    onImageClick: () -> Unit = {},
    onImageLongClick: () -> Unit = {},
) {
    val imageModel = ImageRequest.Builder(LocalContext.current)
        .data(imageUri)
        .size(Size.ORIGINAL)
        .crossfade(true)
        .crossfade(512 + 256)
        .build()

    Box(contentAlignment = Alignment.Center) {
        AsyncImage(
            model = imageModel,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clip(MaterialTheme.shapes.small)
                .fillMaxWidth()
                .heightIn((128 + 32).dp)
                .combinedClickable(
                    onClick = onImageClick,
                    onLongClick = onImageLongClick
                )
        )
    }

}