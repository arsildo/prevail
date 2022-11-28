package com.arsildo.prevail.presentation.components.threadList

import android.graphics.Typeface
import android.os.Build.VERSION.SDK_INT
import android.widget.TextView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.arsildo.prevail.logic.network.models.threads.Thread
import com.arsildo.prevail.presentation.components.shared.ContentCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadCard(
    thread: Thread,
    inFocus: Boolean,
    onClick: () -> Unit,
) {
    ContentCard(
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(.1f)),
                    shape = MaterialTheme.shapes.small,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = "${thread.no}",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (thread.sticky == 1) Icon(
                        Icons.Rounded.PushPin,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    if (thread.closed == 1) Icon(
                        Icons.Outlined.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = thread.now,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }
            Text(
                text = thread.semantic_url,
                style = MaterialTheme.typography.titleMedium
            )
            if (thread.com != null) {
                HtmlText(
                    text = thread.com,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            if (thread.ext != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (inFocus) Color.Cyan else Color.Transparent)
                        .height(300.dp)
                        .padding(vertical = 4.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center
                ) {
                   /* ImageViewer(
                        imageUrl = "https://i.4cdn.org/wsg/${thread.tim}",
                        modifier = Modifier.blur(if (thread.ext == ".gif") 0.dp else 4.dp)
                    )*/
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "${thread.replies} replies",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = "${thread.images} media file(s)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )

            }
        }

    }
}

@Composable
fun ImageViewer(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).components {
        if (SDK_INT >= 28) add(ImageDecoderDecoder.Factory())
        else add(GifDecoder.Factory())
    }.build()

    AsyncImage(
        model = imageUrl,
        imageLoader = imageLoader,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .fillMaxSize()
    )

}


@Composable
fun HtmlText(
    text: String,
    color: Color
) {
    AndroidView(factory = { context ->
        TextView(context).apply {
            setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
            setTextColor(color.toArgb())
            typeface = Typeface.DEFAULT_BOLD
        }
    }
    )
}
