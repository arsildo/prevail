package com.arsildo.prevail.presentation.components

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.arsildo.prevail.logic.network.model.thread_catalog.Thread

@Composable
fun ThreadCard(thread: Thread) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "${thread.no}",
                    color = MaterialTheme.colorScheme.tertiary.copy(.2f),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = thread.now,
                    color = MaterialTheme.colorScheme.tertiary.copy(.2f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            // thread Title
            Text(
                text = thread.semantic_url,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
            // additional text desc
            if (thread.com != null) {
                Text(
                    text = formatLogic(thread.com),
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // add image thumbnail and playable media

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${thread.replies} replies",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${thread.images} media file(s)",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.bodySmall
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
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(top = 64.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "${thread.no}",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = thread.now,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            // thread Title
            Text(
                text = thread.semantic_url,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
            // additional text desc
            Text(
                text = "Checkout the rules and catalog first.",
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}

