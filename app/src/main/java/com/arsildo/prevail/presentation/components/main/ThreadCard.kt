package com.arsildo.prevail.presentation.components.main

import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.arsildo.prevail.logic.network.models.threads.Thread
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun ThreadCard(thread: Thread) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(.4f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${thread.no}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = thread.now,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Text(
                text = thread.semantic_url,
                style = MaterialTheme.typography.titleMedium
            )
            if (thread.com != null) {
                HtmlText(
                    text = thread.com,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            /* MediaPlayer(url = "https://i.4cdn.org/wsg/${thread.tim}${thread.ext}")*/

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${thread.replies} replies",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "${thread.images} media file(s)",
                    style = MaterialTheme.typography.labelSmall
                )

            }
        }

    }
}


@Composable
fun MediaPlayer(url: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            repeatMode = Player.REPEAT_MODE_ALL
        }
    }

    Box(
        modifier = Modifier
            .height(256.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = {
                StyledPlayerView(context).apply {
                    player = exoPlayer
                }
            }
        )
    }

}


@Composable
fun HtmlText(
    text: String,
    color: Color
) {
    AndroidView(factory = { context ->
        TextView(context).apply {
            setText(
                if (text.length > 128) {
                    HtmlCompat.fromHtml(text.take(256) + "...", HtmlCompat.FROM_HTML_MODE_LEGACY)
                } else {
                    HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
                }
            )
            setTextColor(color.toArgb())
        }
    })
}

/*private fun formatLogic(string: String): String {
    val decoded: String = Html
        .fromHtml(string, Html.FROM_HTML_MODE_COMPACT)
        .toString()
    return if (decoded.count() > 128) {
        decoded.take(128) + "..."
    } else decoded
}*/

