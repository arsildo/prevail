package com.arsildo.prevail.utils

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.arsildo.prevail.data.source.PlayerRepository
import com.arsildo.prevail.di.MEDIA_BASE_URL
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.delay

@Composable
fun MediaPlayer(
    mediaID: Long,
    currentBoard: String,
    aspectRatio: Float,
    inFocus: Boolean,
    fullScreenMode: Boolean,
    playerRepository: PlayerRepository,
    onPlayVideoNotInFocus: () -> Unit = {},
) {

    val player = remember { playerRepository.player }
    val isPlaying by remember { playerRepository.isPlaying }
    val isMuted by remember { playerRepository.isMuted }
    val videoDuration by remember { playerRepository.videoDuration }
    var progressMade by remember { playerRepository.progressMade }
    var durationLeft by remember { playerRepository.durationLeft }

    val playerState = playerRepository.playerState.value

    val animatedProgress: Float by animateFloatAsState(
        (if (isPlaying) progressMade else progressMade).toFloat(),
        animationSpec = tween(easing = LinearOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clip(MaterialTheme.shapes.large)
            .aspectRatio(aspectRatio),
        contentAlignment = Alignment.Center
    ) {
        VideoThumbnail(
            preloadedThumbnailUri = "$MEDIA_BASE_URL$currentBoard/$mediaID" + "s.jpg",
            videoUri = "$MEDIA_BASE_URL$currentBoard/$mediaID.webm",
            onPlayVideoNotInFocus = onPlayVideoNotInFocus
        )
        AnimatedVisibility(
            visible = inFocus && playerState == Player.STATE_READY,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            ExoPlayerAndroidView(player)
            VidePlayerControls(
                isPlaying = isPlaying,
                isVideoMuted = isMuted,
                videoDuration = videoDuration,
                videoProgress = animatedProgress,
                durationLeft = durationLeft,
                onPlayOrPauseClick = { playerRepository.pauseUnPausePlayer() },
                onMuteUnMuteClick = playerRepository::muteUnMutePlayer,
            )
            if (isPlaying) LaunchedEffect(Unit) {
                while (true) {
                    durationLeft = videoDuration - player.currentPosition
                    progressMade = 1.0 - (player.currentPosition.toDouble() / videoDuration)
                    delay(1000)
                }
            }

        }

        if (playerState == Player.STATE_BUFFERING && inFocus) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, strokeWidth = 3.dp)
        }

    }

}

@Composable
private fun ExoPlayerAndroidView(
    exoPlayer: Player,
    context: Context = LocalContext.current,
) {
    AndroidView(
        factory = {
            StyledPlayerView(context).apply {
                useController = false
                player = exoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
        },
        update = { it.player = exoPlayer },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun VidePlayerControls(
    isPlaying: Boolean,
    isVideoMuted: Boolean,
    videoDuration: Long,
    durationLeft: Long,
    videoProgress: Float,
    onPlayOrPauseClick: () -> Unit,
    onMuteUnMuteClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onPlayOrPauseClick),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = !isPlaying,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            IconButton(
                onClick = onPlayOrPauseClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.White.copy(.2f)
                ),
            ) {
                Icon(
                    Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isPlaying) durationLeft.formatMinSec() else videoDuration.formatMinSec(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(Color.Black.copy(.2f))
                    .padding(vertical = 2.dp, horizontal = 4.dp)
                    .animateContentSize()
            )
            IconButton(onClick = onMuteUnMuteClick) {
                Icon(
                    imageVector = if (isVideoMuted) Icons.Rounded.VolumeOff else Icons.Rounded.VolumeUp,
                    contentDescription = null,
                    tint = if (isVideoMuted) Color.White.copy(.5f) else Color.White,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(.2f))
                        .padding(4.dp)
                        .size(16.dp)

                )
            }
        }

        LinearProgressIndicator(
            progress = videoProgress,
            color = Color.White.copy(.8f),
            backgroundColor = MaterialTheme.colorScheme.primary.copy(.2f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(4.dp)
                .padding(horizontal = 16.dp)
                .clip(MaterialTheme.shapes.small)
        )
    }

}
