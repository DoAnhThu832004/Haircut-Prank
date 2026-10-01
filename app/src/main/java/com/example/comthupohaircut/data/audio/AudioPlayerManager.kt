package com.example.comthupohaircut.data.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayerManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isLooping = MutableStateFlow(false)
    val isLooping: StateFlow<Boolean> = _isLooping.asStateFlow()

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private var vibrationEnabled = true

    fun setVibrationEnabled(enabled: Boolean) {
        vibrationEnabled = enabled
        if (!enabled) {
            stopVibration()
        }
    }

    fun play(
        filePath: String,
        loop: Boolean = _isLooping.value,
        onFinished: (() -> Unit)? = null
    ) {
        stop()

        val file = File(filePath)
        if (!file.exists()) return

        try {
            _isLooping.value = loop
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                setDataSource(filePath)
                isLooping = loop
                setOnCompletionListener {
                    if (!isLooping) {
                        _isPlaying.value = false
                        stopVibration()
                        onFinished?.invoke()
                    }
                }
                prepare()
                start()
            }
            _isPlaying.value = true
            if (vibrationEnabled) {
                startVibration(loop)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stop()
        }
    }

    fun setLoop(loop: Boolean) {
        _isLooping.value = loop
        mediaPlayer?.isLooping = loop
    }

    fun stop() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                reset()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
            _isPlaying.value = false
            stopVibration()
        }
    }

    private fun startVibration(loop: Boolean) {
        try {
            vibrator?.let { v ->
                if (!v.hasVibrator()) return
                val pattern = longArrayOf(0, 200, 100, 200, 300)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = VibrationEffect.createWaveform(pattern, if (loop) 0 else -1)
                    v.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    v.vibrate(pattern, if (loop) 0 else -1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopVibration() {
        try {
            vibrator?.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        stop()
    }
}