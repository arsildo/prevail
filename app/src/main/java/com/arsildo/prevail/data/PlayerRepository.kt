package com.arsildo.prevail.data

import androidx.compose.runtime.mutableStateOf
import com.arsildo.prevail.di.CURRENT_BOARD
import com.arsildo.prevail.di.MEDIA_BASE_URL
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import javax.inject.Inject


class PlayerRepository @Inject constructor(
    private val player: ExoPlayer
) {

    var isPlaying = mutableStateOf(false)
    var playerStateListener = mutableStateOf(1)
    var isMuted = mutableStateOf(false)

    var videoDuration = mutableStateOf(0L)

    fun clearPlayerResources() {
        player.pause()
        player.clearMediaItems()
        player.clearVideoSurface()
        player.removeMediaItem(0)
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
    }


    val listener = object : Player.Listener {
        override fun onIsPlayingChanged(_isPlaying: Boolean) {
            super.onIsPlayingChanged(_isPlaying)
            isPlaying.value = _isPlaying
        }

        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)
            videoDuration.value = player.duration
            playerStateListener.value = player.playbackState
        }

        override fun onIsLoadingChanged(_isLoading: Boolean) {
            super.onIsLoadingChanged(_isLoading)
        }


        override fun onVolumeChanged(volume: Float) {
            super.onVolumeChanged(volume)
            isMuted.value = volume == 0f
        }

    }

    init {
        player.playWhenReady = false
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.addListener(listener)
    }

}