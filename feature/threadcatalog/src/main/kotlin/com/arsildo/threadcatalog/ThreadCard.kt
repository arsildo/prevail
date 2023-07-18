package com.arsildo.threadcatalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arsildo.media.Image
import com.arsildo.media.VideoThumbnail
import com.arsildo.model.Thread

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ThreadCard(
    thread: Thread,
    onClick: () -> Unit,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Badge { Text(text = thread.no.toString()) }
                Text(text = thread.now.toString(), style = MaterialTheme.typography.labelMedium)
            }

            Text(
                text = thread.sub.toString(),
            )

            Box(
                modifier = Modifier.aspectRatio(1f)
            ) {
                Image(url = "https://i.4cdn.org/po/1546293948883.png")
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = thread.replies.toString() + " replies",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}