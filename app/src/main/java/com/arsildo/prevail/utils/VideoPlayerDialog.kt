package com.arsildo.prevail.utils

import android.content.Context
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.platform.LocalDensity
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

    var durationLeft by remember { mutableStateOf(1L) }
    var progress by remember { mutableStateOf(1.0) }
    val animatedProgress: Float by animateFloatAsState(
        if (viewModel.playerRepository.isPlaying.value) progress.toFloat() else progress.toFloat(),
        animationSpec = tween(easing = LinearOutSlowInEasing)
    )

    var videoHeight by remember { mutableStateOf(1) }

    fun onDismiss() {
        viewModel.clearPlayer()
        visible.value = !visible.value
    }


    AnimatedVisibility(
        visible = visible.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        Dialog(
            onDismissRequest = ::onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(.01f))
                    .padding(horizontal = 16.dp)
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                if (!viewModel.playerRepository.isLoading.value) {
                    Box(
                        modifier = Modifier
                            .heightIn(with(LocalDensity.current) { videoHeight.toDp() })
                            .clickable { if (viewModel.playerRepository.isPlaying.value) player.pause() else player.play() },
                        contentAlignment = Alignment.Center
                    ) {
                        ExoPlayerAndroidView(exoPlayer = player)
                        VidePlayerControls(
                            isPlaying = viewModel.playerRepository.isPlaying.value,
                            isVideoMuted = viewModel.playerRepository.isMuted.value,
                            videoDuration = viewModel.playerRepository.videoDuration.value,
                            videoProgress = animatedProgress,
                            timeLeft = durationLeft,
                            onPlayOrPauseClick = { if (viewModel.playerRepository.isPlaying.value) player.pause() else player.play() },
                            onMuteUnMuteClick =
                            { if (viewModel.playerRepository.isMuted.value) viewModel.unMutePlayer() else viewModel.mutePlayer() },
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                } else
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(.2f))
                            .padding(4.dp)
                    )
            }
        }
    }

    if (viewModel.playerRepository.isPlaying.value) LaunchedEffect(Unit) {
        while (true) {
            durationLeft = player.duration - player.currentPosition
            progress = 1.0 - (player.currentPosition.toDouble() / player.duration)
            delay(1000)
        }
    }

    DisposableEffect(Unit) {
        onDispose { player.removeListener(viewModel.playerRepository.listener) }
    }

    BackHandler { onDismiss() }

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
                player = exoPlayer
            }
        }
    )
}
