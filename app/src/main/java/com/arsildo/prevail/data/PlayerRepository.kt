package com.arsildo.prevail.data

import com.arsildo.prevail.di.CURRENT_BOARD
import com.arsildo.prevail.di.MEDIA_BASE_URL
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import javax.inject.Inject


class PlayerRepository @Inject constructor(
    private val player: ExoPlayer
) {

    init {
        player.repeatMode = Player.REPEAT_MODE_ONE
    }

    fun clearPlayerResources() {
        player.pause()
        player.clearMediaItems()
        player.clearVideoSurface()
    }

    fun mutePlayer() {
        player.volume = 0f
    }

    fun unMutePlayer() {
        player.volume = 1f
    }

    fun playMediaFile(mediaID: Long) {
        val uri = "$MEDIA_BASE_URL$CURRENT_BOARD$mediaID.webm"
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = false
    }

}