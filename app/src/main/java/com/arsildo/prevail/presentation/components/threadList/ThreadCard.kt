package com.arsildo.prevail.presentation.components.threadList

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arsildo.prevail.logic.network.models.threadList.Thread
import com.arsildo.prevail.presentation.components.shared.ContentCard
import com.arsildo.prevail.presentation.components.shared.GIFMedia
import com.arsildo.prevail.presentation.components.shared.HtmlText
import com.arsildo.prevail.presentation.components.shared.ImageMedia
import com.arsildo.prevail.presentation.components.shared.MediaController
import com.arsildo.prevail.presentation.components.shared.getCountryFromCode

@Composable
fun ThreadCard(
    thread: Thread,
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
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),

                    ) {

                    Text(
                        text = "no. ${thread.no}",
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
                   Column(horizontalAlignment = Alignment.End) {
                       Text(
                           text = thread.name + if (thread.country != null) " " + getCountryFromCode(thread.country) else "",
                           style = MaterialTheme.typography.labelSmall,
                           color = MaterialTheme.colorScheme.tertiary,
                       )
                       Text(
                           text = thread.now,
                           fontSize = 10.sp,
                           fontWeight = FontWeight.Bold,
                           color = MaterialTheme.colorScheme.onBackground,
                       )
                   }
                }
            }
            Text(
                text = thread.semantic_url.replace("-", " "),
                style = MaterialTheme.typography.titleMedium,
            )
            if (thread.com != null) {
                HtmlText(
                    text = thread.com,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            if (thread.ext != null) {
                MediaController(mediaType = thread.ext, mediaID = thread.tim)
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

