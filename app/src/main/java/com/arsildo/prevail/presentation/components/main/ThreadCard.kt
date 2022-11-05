package com.arsildo.prevail.presentation.components.main

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.arsildo.prevail.logic.network.models.threads.Thread
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun ThreadCard(thread: Thread) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${thread.no}",)
                Text(text = thread.now,)
            }
            Text(text = thread.semantic_url,)
            if (thread.com != null) { Text(text = thread.com, maxLines = 2) }

            /* MediaPlayer(url = "https://i.4cdn.org/wsg/${thread.tim}${thread.ext}")*/

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${thread.replies} replies",)
                Text(text = "${thread.images} media file(s)",)

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

/*private fun formatLogic(string: String): String {
    val decoded: String = Html
        .fromHtml(string, Html.FROM_HTML_MODE_COMPACT)
        .toString()
    return if (decoded.count() > 128) {
        decoded.take(128) + "..."
    } else decoded
}*/

