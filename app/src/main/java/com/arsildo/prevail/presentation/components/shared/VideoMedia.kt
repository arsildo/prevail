package com.arsildo.prevail.presentation.components.shared

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun VideoMedia(
) {
    /*ExoPlayerAndroidView(exoPlayer = , context = )*/
}

@Composable
fun ExoPlayerAndroidView(
    exoPlayer: ExoPlayer,
    context: Context,
) {
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
}
