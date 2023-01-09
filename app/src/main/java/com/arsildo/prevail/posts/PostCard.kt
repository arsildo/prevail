package com.arsildo.prevail.posts

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
import com.arsildo.prevail.data.source.PlayerRepository
import com.arsildo.prevail.data.models.Post
import com.arsildo.prevail.utils.ContentCardWrapper
import com.arsildo.prevail.utils.HtmlText
import com.arsildo.prevail.utils.MediaTypeIdentifier
import com.arsildo.prevail.utils.getCountryFromCode

@Composable
fun PostCard(
    post: Post,
    playerRepository: PlayerRepository,
    inFocus: Boolean,
    onPlayVideoNotInFocus: (Long,Float) -> Unit,
) {
    ContentCardWrapper {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text(
                        text = post.no.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = post.name + if (post.country != null) " " + getCountryFromCode(post.country) else "",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = post.now,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
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
            if (post.ext != null) {
                MediaTypeIdentifier(
                    mediaType = post.ext,
                    mediaHeight = post.h,
                    mediaWidth = post.w,
                    mediaID = post.tim,
                    inFocus = inFocus,
                    playerRepository = playerRepository,
                    onPlayVideoNotInFocus = { aspectRatio -> onPlayVideoNotInFocus(post.tim,aspectRatio) }
                )
            }

        }
    }
}