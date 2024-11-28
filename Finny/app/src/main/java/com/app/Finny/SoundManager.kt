package com.app.Finny

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool

object SoundManager {
    var mediaPlayer: MediaPlayer? = null
    private var soundPool: SoundPool? = null
    private val soundMap = HashMap<String, Int>()

    var musicVolume = 1.0f // Default music volume (0.0f to 1.0f)
    var sfxVolume = 1.0f // Default SFX volume (0.0f to 1.0f)
    var isMusicEnabled = true // Flag for music
    var isSfxEnabled = true // Flag for SFX

    fun initializeVolumes(context: Context) {
        val sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        musicVolume = sharedPreferences.getInt("musicVolume", 50) / 100.0f
        sfxVolume = sharedPreferences.getInt("sfxVolume", 50) / 100.0f
        isMusicEnabled = sharedPreferences.getBoolean("isMusicEnabled", true)
        isSfxEnabled = sharedPreferences.getBoolean("isSfxEnabled", true)
    }

    private fun ensureSoundPoolInitialized(context: Context) {
        if (soundPool == null) {
            soundPool = SoundPool.Builder().setMaxStreams(5).build()
            soundMap["answer_click"] = soundPool!!.load(context, R.raw.answer_click, 1)
            soundMap["complete"] = soundPool!!.load(context, R.raw.complete, 1)
            soundMap["start_game"] = soundPool!!.load(context, R.raw.start_game, 1)
        }
    }

    fun playSong(context: Context, songResId: Int, loop: Boolean) {
        if (!isMusicEnabled) return
        stopSong()
        mediaPlayer = MediaPlayer.create(context, songResId)
        mediaPlayer?.setVolume(musicVolume, musicVolume)
        mediaPlayer?.isLooping = loop
        mediaPlayer?.start()
    }

    fun stopSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun playSFX(context: Context, sfxName: String) {
        if (!isSfxEnabled) return
        ensureSoundPoolInitialized(context)
        when (sfxName) {
            "complete" -> { // Use MediaPlayer for longer SFX
                val mediaPlayer = MediaPlayer.create(context, R.raw.complete)
                mediaPlayer.setVolume(sfxVolume, sfxVolume)
                mediaPlayer.setOnCompletionListener { mp -> mp.release() }
                mediaPlayer.start()
            }
            else -> { // Use SoundPool for shorter SFX
                soundPool?.play(soundMap[sfxName] ?: return, sfxVolume, sfxVolume, 1, 0, 1.0f)
            }
        }
    }

    fun release() {
        mediaPlayer?.release()
        soundPool?.release()
        mediaPlayer = null
        soundPool = null
    }
}