package com.arsildo.prevail.presentation.components.threadPosts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arsildo.prevail.logic.network.models.threadPosts.Post
import com.arsildo.prevail.presentation.components.shared.ContentCard
import com.arsildo.prevail.presentation.components.shared.HtmlText
import com.arsildo.prevail.presentation.components.shared.MediaController
import com.arsildo.prevail.presentation.components.shared.getCountryFromCode
import com.google.android.exoplayer2.ExoPlayer

@Composable
fun PostCard(
    post: Post,
    inFocus: Boolean,
    exoPlayer: ExoPlayer,
    onPositionSwitched: () -> Unit,
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
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                ) {
                    Text(
                        text = "no. ${post.no}",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = post.name + if (post.country != null) " " + getCountryFromCode(post.country) else "",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Text(
                        text = post.now,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }

            if (post.com != null) {
                HtmlText(text = post.com, color = MaterialTheme.colorScheme.tertiary)
            }

            if (post.ext != null) {
                MediaController(mediaType = post.ext, mediaID = post.tim)
            }

        }
    }
}