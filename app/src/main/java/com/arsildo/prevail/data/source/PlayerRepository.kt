package com.arsildo.prevail.data.source

import androidx.compose.runtime.mutableStateOf
import com.arsildo.prevail.di.MEDIA_BASE_URL
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import javax.inject.Inject


class PlayerRepository @Inject constructor(val player: ExoPlayer) {

    var isPlaying = mutableStateOf(false)
    var isMuted = mutableStateOf(false)

    var videoDuration = mutableStateOf(0L)
    var durationLeft = mutableStateOf(1L)
    var progressMade = mutableStateOf(1.0)

    var playerState = mutableStateOf(1)

    init {

        player.addListener(
            object : Player.Listener {
                override fun onIsPlayingChanged(_isPlaying: Boolean) {
                    super.onIsPlayingChanged(_isPlaying)
                    isPlaying.value = _isPlaying
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    videoDuration.value = player.duration
                    progressMade.value = 1.0
                    when (playbackState) {
                        Player.STATE_IDLE -> playerState.value = Player.STATE_IDLE
                        Player.STATE_BUFFERING -> playerState.value = Player.STATE_BUFFERING
                        Player.STATE_READY -> playerState.value = Player.STATE_READY
                        Player.STATE_ENDED -> playerState.value = Player.STATE_ENDED
                    }
                }


                override fun onVolumeChanged(volume: Float) {
                    super.onVolumeChanged(volume)
                    isMuted.value = volume == 0f
                }

                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    player.clearMediaItems()
                }
            }
        )
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        player.setForegroundMode(false)
    }

    fun playMediaFile(currentBoard: String, mediaID: Long) {
        val uri = "$MEDIA_BASE_URL$currentBoard/$mediaID.webm"
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = false
    }

    fun muteUnMutePlayer() {
        if (isMuted.value) player.volume = 1f else player.volume = 0f
    }


    fun pauseUnPausePlayer() {
        if (isPlaying.value) player.pause() else player.play()
    }

    fun clearPlayer() {
        player.clearMediaItems()
        player.clearVideoSurface()
    }
}