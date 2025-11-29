package com.slotchristmas.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.slotchristmas.config.AssetConfig

class AudioManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var soundPool: SoundPool? = null
    private val soundIds = mutableMapOf<SoundEffect, Int>()
    private val activeStreams = mutableMapOf<SoundEffect, Int>()

    private var isMuted = false
    private var musicVolume = 0.7f
    private var sfxVolume = 1.0f
    private var isInitialized = false

    fun initialize() {
        if (isInitialized) return

        // Initialize SoundPool for SFX
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(8)
            .setAudioAttributes(audioAttributes)
            .build()

        // Preload all sound effects
        soundPool?.let { pool ->
            SoundEffect.entries.forEach { effect ->
                try {
                    soundIds[effect] = pool.load(context, effect.resId, 1)
                } catch (e: Exception) {
                    // Sound file might not exist yet (placeholder)
                }
            }
        }

        // Initialize MediaPlayer for background music
        try {
            mediaPlayer = MediaPlayer.create(context, AssetConfig.BACKGROUND_MUSIC)?.apply {
                isLooping = true
                setVolume(musicVolume, musicVolume)
            }
        } catch (e: Exception) {
            // Music file might not exist yet (placeholder)
        }

        isInitialized = true
    }

    fun startMusic() {
        if (!isMuted) {
            try {
                mediaPlayer?.start()
            } catch (e: Exception) {
                // Ignore if not ready
            }
        }
    }

    fun pauseMusic() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun resumeMusic() {
        if (!isMuted) {
            try {
                mediaPlayer?.start()
            } catch (e: Exception) {
                // Ignore if not ready
            }
        }
    }

    fun playSfx(effect: SoundEffect, loop: Boolean = false) {
        if (isMuted) return

        val soundId = soundIds[effect] ?: return
        val loopFlag = if (loop) -1 else 0

        soundPool?.let { pool ->
            val streamId = pool.play(
                soundId,
                sfxVolume,
                sfxVolume,
                1,
                loopFlag,
                1.0f
            )
            if (loop && streamId != 0) {
                activeStreams[effect] = streamId
            }
        }
    }

    fun stopSfx(effect: SoundEffect) {
        activeStreams[effect]?.let { streamId ->
            soundPool?.stop(streamId)
        }
        activeStreams.remove(effect)
    }

    fun stopAllSfx() {
        activeStreams.forEach { (_, streamId) ->
            soundPool?.stop(streamId)
        }
        activeStreams.clear()
    }

    fun setMuted(muted: Boolean) {
        isMuted = muted
        if (muted) {
            mediaPlayer?.setVolume(0f, 0f)
            stopAllSfx()
        } else {
            mediaPlayer?.setVolume(musicVolume, musicVolume)
        }
    }

    fun isMuted(): Boolean = isMuted

    fun toggleMute(): Boolean {
        setMuted(!isMuted)
        return isMuted
    }

    fun setMusicVolume(volume: Float) {
        musicVolume = volume.coerceIn(0f, 1f)
        if (!isMuted) {
            mediaPlayer?.setVolume(musicVolume, musicVolume)
        }
    }

    fun setSfxVolume(volume: Float) {
        sfxVolume = volume.coerceIn(0f, 1f)
    }

    fun release() {
        stopAllSfx()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {
            // Ignore
        }
        mediaPlayer = null

        soundPool?.release()
        soundPool = null

        soundIds.clear()
        activeStreams.clear()
        isInitialized = false
    }
}
