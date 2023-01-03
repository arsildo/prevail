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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.arsildo.prevail.data.PlayerRepository
import com.arsildo.prevail.di.CURRENT_BOARD
import com.arsildo.prevail.di.MEDIA_BASE_URL
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.delay

@Composable
fun MediaPlayer(
    mediaID: Long,
    aspectRatio: Float,
    inFocus: Boolean,
    playerRepository: PlayerRepository,
    onPlayVideoNotInFocus: () -> Unit = {},
) {

    val player = playerRepository.player
    val isPlaying = playerRepository.isPlaying.value
    val isMuted = playerRepository.isMuted.value
    val videoDuration = playerRepository.videoDuration.value
    val progressMade = playerRepository.progressMade.value.toFloat()
    val durationLeft = playerRepository.durationLeft.value

    val playerState = playerRepository.playerState.value
    fun muteUnMute() = playerRepository.muteUnMutePlayer()

    val animatedProgress: Float by animateFloatAsState(
        if (isPlaying) progressMade else progressMade,
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
            preloadedThumbnailUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID" + "s.jpg",
            videoUri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID.webm",
            onPlayVideoNotInFocus = onPlayVideoNotInFocus
        )
        AnimatedVisibility(
            visible = inFocus && playerState == Player.STATE_READY,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            ExoPlayerAndroidView(playerRepository.player)
            VidePlayerControls(
                isPlaying = isPlaying,
                isVideoMuted = isMuted,
                videoDuration = videoDuration,
                videoProgress = animatedProgress,
                timeLeft = durationLeft,
                onPlayOrPauseClick = { if (inFocus) playerRepository.pauseUnPausePlayer() else onPlayVideoNotInFocus() },
                onMuteUnMuteClick = ::muteUnMute,
            )
            if (isPlaying) LaunchedEffect(Unit) {
                while (true) {
                    playerRepository.durationLeft.value = videoDuration - player.currentPosition
                    playerRepository.progressMade.value = 1.0 - (player.currentPosition.toDouble() / videoDuration)
                    delay(1000)
                }
            }

        }

        if (playerState == Player.STATE_BUFFERING && inFocus) {
            CircularProgressIndicator(
                color = Color.White.copy(.2f),
                strokeWidth = 2.dp,
            )
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
        modifier = Modifier.fillMaxSize().animateContentSize(tween(durationMillis = 128))
    )
}

@Composable
private fun VidePlayerControls(
    isPlaying: Boolean,
    isVideoMuted: Boolean,
    videoDuration: Long,
    timeLeft: Long,
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
            exit = fadeOut(tween(delayMillis = 64)),
        ) {
            IconButton(
                onClick = onPlayOrPauseClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(.2f)
                ),
            ) {
                Icon(
                    Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
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
                text = if (isPlaying) timeLeft.formatMinSec() else videoDuration.formatMinSec(),
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
