package com.arsildo.prevail.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.unit.dp
import com.arsildo.prevail.data.Post
import com.arsildo.prevail.utils.ContentCardWrapper
import com.arsildo.prevail.utils.HtmlText
import com.arsildo.prevail.utils.MediaTypeDistributor
import com.arsildo.prevail.utils.countryCodeToEmoji

@Composable
fun PostCard(
    post: Post,
    playableMedia: @Composable (BoxScope.() -> Unit)
) {
    ContentCardWrapper {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text(
                        text = post.no.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = post.name + if (post.country != null) " " + countryCodeToEmoji(post.country) else "",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = post.now,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
            if (post.sub != null) {
                HtmlText(htmlText = post.sub)
            }
            if (post.com != null) {
                HtmlText(htmlText = post.com)
            }
            if (post.mediaType != null) {
                MediaTypeDistributor(
                    mediaType = post.mediaType,
                    mediaID = post.mediaID,
                    mediaHeight = post.mediaHeight,
                    mediaWidth = post.mediaWidth,
                    playableMedia = playableMedia
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (post.replies > 0) Text(
                    text = "${post.replies} replies",
                    style = MaterialTheme.typography.labelSmall,
                )
            }

        }
    }
}