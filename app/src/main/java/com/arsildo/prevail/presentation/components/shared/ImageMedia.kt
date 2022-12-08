package com.arsildo.prevail.presentation.components.shared

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import coil.size.SizeResolver

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageMedia(
    id: Long,
    type: String,
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://i.4cdn.org/sp/$id$type")
            .crossfade(true)
            .size(SizeResolver(Size.ORIGINAL))
            .scale(Scale.FIT)
            .build(),
        loading = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(32.dp).padding(end = 4.dp)
                )
                Text(
                    text = "Loading Image...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clip(MaterialTheme.shapes.small)
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = {}
            )
    )
}