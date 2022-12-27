package com.arsildo.prevail.utils

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Share
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.arsildo.prevail.threads.ThreadsViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VideoPlayerDialog(
    visible: MutableState<Boolean>,
    player: ExoPlayer,
    viewModel: ThreadsViewModel,
) {

    var playerState by remember { mutableStateOf(1) }
    var isPlaying by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var videoDuration by remember { mutableStateOf(0L) }
    var durationLeft by remember { mutableStateOf(1L) }

    var progress by remember { mutableStateOf(1.0) }
    val animatedProgress: Float by animateFloatAsState(
        if (isPlaying) progress.toFloat() else progress.toFloat(),
        animationSpec = tween(easing = LinearOutSlowInEasing)
    )

    fun exitDialogAndClearPlayer() {
        viewModel.clearPlayer()
        visible.value = !visible.value
    }

    if (visible.value) {
        Dialog(
            onDismissRequest = { exitDialogAndClearPlayer() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {

            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = playerState==Player.STATE_READY,
                    enter = fadeIn(tween(durationMillis = 1000)),
                    exit = fadeOut(),
                    modifier = Modifier.animateContentSize(tween(durationMillis = 1000))
                ) {

                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .clickable { if (isPlaying) player.pause() else player.play() },
                        contentAlignment = Alignment.Center
                    ) {

                        ExoPlayerAndroidView(exoPlayer = player)

                        VidePlayerControls(
                            isPlaying = isPlaying,
                            isVideoMuted = isMuted,
                            videoDuration = videoDuration,
                            videoProgress = animatedProgress,
                            timeLeft = durationLeft,
                            onPlayOrPauseClick = { if (isPlaying) player.pause() else player.play() },
                            onMuteUnMuteClick = { if (isMuted) viewModel.unMutePlayer() else viewModel.mutePlayer() },
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround

                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Rounded.Download, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Rounded.Share, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(
                        onClick = { player.seekTo(0) },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Replay,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    IconButton(
                        onClick = { exitDialogAndClearPlayer() },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                if (playerState == Player.STATE_BUFFERING || playerState == Player.STATE_IDLE) CircularProgressIndicator()

            }

            /* Box(
                 modifier = Modifier
                     .padding(start = 16.dp, end = 16.dp, top = 32.dp)
                     .clip(MaterialTheme.shapes.medium)
                     .background(backgroundColor)
                     .padding(16.dp)
                     .fillMaxSize(),
                 contentAlignment = Alignment.Center
             ) {

                 AnimatedVisibility(
                     visible = playerState == Player.STATE_READY,
                     enter = fadeIn(tween(delayMillis = 256, durationMillis = 1000)),
                     exit = fadeOut(),
                     modifier = Modifier
                         .align(Alignment.Center)
                         .clip(MaterialTheme.shapes.medium)
                         .background(MaterialTheme.colorScheme.background)
                         .padding(4.dp)
                         .clickable { if (isPlaying) player.pause() else player.play() },
                 ) {
                     *//*Box(contentAlignment = Alignment.Center) {
                        ExoPlayerAndroidView(exoPlayer = player)

                        VidePlayerControls(
                            isPlaying = isPlaying,
                            isVideoMuted = isMuted,
                            videoDuration = videoDuration,
                            videoProgress = animatedProgress,
                            timeLeft = durationLeft,
                            onPlayOrPauseClick = { if (isPlaying) player.pause() else player.play() },
                            onMuteUnMuteClick = { if (isMuted) viewModel.unMutePlayer() else viewModel.mutePlayer() },
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }*//*

                }





            }*/
        }
    }

    if (isPlaying) LaunchedEffect(Unit) {
        while (true) {
            durationLeft = player.duration - player.currentPosition
            progress =
                1.0 - (player.currentPosition.toDouble() / player.duration)
            delay(1000)
        }
    }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {

            override fun onIsPlayingChanged(_isPlaying: Boolean) {
                super.onIsPlayingChanged(_isPlaying)
                isPlaying = _isPlaying
            }


            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                isMuted = when (player.volume) {
                    1f -> false
                    0f -> true
                    else -> false
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                videoDuration = player.duration
                progress = 1.0
                when (playbackState) {
                    Player.STATE_IDLE -> Player.STATE_IDLE
                    Player.STATE_BUFFERING -> Player.STATE_BUFFERING
                    Player.STATE_READY -> playerState = Player.STATE_READY
                    Player.STATE_ENDED -> playerState = Player.STATE_ENDED
                }
            }
        }
        player.addListener(listener)
        onDispose { player.removeListener(listener) }
    }

    BackHandler { exitDialogAndClearPlayer() }

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
    modifier: Modifier
) {
    AnimatedVisibility(
        visible = !isPlaying,
        enter = fadeIn(),
        exit = fadeOut(tween(delayMillis = 64)),
    ) {
        IconButton(
            onClick = onPlayOrPauseClick,
            modifier = modifier
                .clip(CircleShape)
                .background(Color.Black.copy(.2f))
        ) {
            Icon(
                Icons.Rounded.PlayArrow,
                contentDescription = null,
                tint = Color.White,
            )
        }
    }

    Row(
        modifier = modifier
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
                tint = Color.White,
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
        backgroundColor = Color.Black.copy(.2f),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(MaterialTheme.shapes.extraSmall)
    )

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
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }
        },
        update = { it.player = exoPlayer },
    )
}
