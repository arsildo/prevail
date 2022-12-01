package com.arsildo.prevail.presentation.components.shared

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Composable
fun MediaPlayer(
    exoPlayer: ExoPlayer,
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(exoPlayer.isPlaying) }
    var muteAudio by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(0L) }
    var videoDuration by remember { mutableStateOf(exoPlayer.currentPosition / exoPlayer.duration) }
    var progress by remember { mutableStateOf(0F) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { if (isPlaying) exoPlayer.pause() else exoPlayer.play() }
    ) {

        DisposableEffect(Unit) {
            val listener = object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying_: Boolean) {
                    isPlaying = isPlaying_
                }

                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    videoDuration = player.contentDuration
                }
            }
            exoPlayer.addListener(listener)
            onDispose { exoPlayer.removeListener(listener) }
        }

        if (isPlaying)
            LaunchedEffect(Unit) {
                while (true) {
                    timeLeft = exoPlayer.duration - exoPlayer.currentPosition
                    progress = (exoPlayer.duration - exoPlayer.currentPosition) / (exoPlayer.duration).toFloat()
                    Log.d("TAGGED" ,"$progress")
                    delay(1000)
                }
            }

        AndroidView(
            factory = {
                StyledPlayerView(context).apply {
                    useController = false
                    player = exoPlayer
                    resizeMode = RESIZE_MODE_ZOOM
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
        )

        MediaPlayerControls(
            isPlaying = isPlaying,
            progress = progress,
            pausePlayVideoClicked = { if (isPlaying) exoPlayer.pause() else exoPlayer.play() },
            audioMuted = muteAudio,
            onMuteVideoClicked = {
                muteAudio = !muteAudio
                if (muteAudio) exoPlayer.volume = 0f else exoPlayer.volume = 1f
            },
            time = if (isPlaying) {
                timeLeft.formatMinSec()
            } else {
                if (timeLeft == 0L || timeLeft < 0L) videoDuration.formatMinSec() else timeLeft.formatMinSec() + " / " + videoDuration.formatMinSec()
            }
        )
    }
}


@Composable
fun MediaPlayerControls(
    isPlaying: Boolean,
    progress: Float,
    pausePlayVideoClicked: () -> Unit,
    audioMuted: Boolean,
    onMuteVideoClicked: () -> Unit,
    time: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = !isPlaying,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center),
        ) {
            IconButton(onClick = pausePlayVideoClicked) {
                Icon(
                    Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(Color.Black.copy(.4f))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 512,
                                easing = LinearOutSlowInEasing
                            )
                        )

                )
                IconButton(onClick = onMuteVideoClicked) {
                    Icon(
                        if (audioMuted) Icons.Rounded.VolumeOff else Icons.Rounded.VolumeUp,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }

            }
            LinearProgressIndicator(
                progress = progress,
                color = MaterialTheme.colorScheme.primary.copy(.5f),
                backgroundColor = Color.Black.copy(.4f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
            )
        }
    }
}

fun Long.formatMinSec(): String {
    return if (this == 0L) "   "
    else if (this < 0L) "Loading... "
    else String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(this)
        )
    )
}