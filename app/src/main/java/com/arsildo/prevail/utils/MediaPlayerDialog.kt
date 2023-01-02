package com.arsildo.prevail.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.arsildo.prevail.data.PlayerRepository

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MediaPlayerDialog(
    visible: Boolean,
    videoAspectRatio: Float,
    onDismissRequest: () -> Unit,
    playerRepository: PlayerRepository,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(),
        exit = fadeOut()
    ) {

        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .shadow(4.dp)
                    .wrapContentHeight()
                    .background(MaterialTheme.colors.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable(onClick = onDismissRequest),
                contentAlignment = Alignment.Center,
                propagateMinConstraints = false
            ) {
                Column(verticalArrangement = Arrangement.SpaceBetween) {

                    MediaPlayer(
                        mediaID = 1,
                        aspectRatio = videoAspectRatio,
                        inFocus = true,
                        playerRepository = playerRepository,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Rounded.Download, contentDescription = null)
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Rounded.Share, contentDescription = null)
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Rounded.Bookmark, contentDescription = null)
                        }
                    }

                }

            }
        }
    }
}

