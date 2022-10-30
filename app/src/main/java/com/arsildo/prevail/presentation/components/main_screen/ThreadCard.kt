package com.arsildo.prevail.presentation.components

import android.net.Uri
import android.text.Html
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.arsildo.prevail.logic.network.model.thread_catalog.Thread
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun ThreadCard(thread: Thread) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(.2f),
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${thread.no}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = thread.now,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Text(
                text = thread.semantic_url,
                style = MaterialTheme.typography.bodyLarge
            )
            if (thread.com != null) {
                Text(
                    text = formatLogic(thread.com),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // add image thumbnail and playable media

            val videoURL =
                "https://i.4cdn.org/wsg/${thread.tim}${thread.ext}"
            val context = LocalContext.current

            val exoPlayer = remember(context) {
                ExoPlayer.Builder(context).build().apply {

                    val mediaItem = MediaItem.Builder()
                        .setUri(Uri.parse(videoURL))
                        .build()
                    setMediaItem(mediaItem)

                    playWhenReady = false
                    prepare()
                }
            }

            AndroidView(
                factory = {
                    StyledPlayerView(context).apply {
                        player = exoPlayer
                        useArtwork = false
                        setShowPreviousButton(false)
                        setShowNextButton(false)
                        setShowRewindButton(false)
                        setShowFastForwardButton(false)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .padding(vertical = 4.dp)
            )

            /* Icon(
                 painterResource(id = R.drawable.ic_launcher_foreground),
                 contentDescription = null,
                 modifier=Modifier.fillMaxWidth().height(256.dp)
             )
 */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${thread.replies} replies",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    text = "${thread.images} media file(s)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )

            }


        }
    }
}


private fun formatLogic(string: String): String {
    val decoded: String = Html
        .fromHtml(string, Html.FROM_HTML_MODE_COMPACT)
        .toString()
    return if (decoded.count() > 128) {
        decoded.take(128) + "..."
    } else decoded
}

@Composable
fun ThreadRulesCard(thread: Thread) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${thread.no}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = thread.now,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = thread.semantic_url,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Checkout the rules and catalog first.",
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}

