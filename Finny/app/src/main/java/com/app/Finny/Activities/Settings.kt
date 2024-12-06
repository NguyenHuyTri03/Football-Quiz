package com.app.Finny.Activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.R
import com.app.Finny.SoundManager
import com.app.Finny.databinding.ActivitySettingsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Settings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private var auth = Firebase.auth

    private lateinit var exitDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bindings
        buttonBinding()
        soundBinding()

        // Create a an logout confirmation popup
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle("Logout?")
            // Return the user to the home screen
            .setPositiveButton("Yes") { _, _ ->
                auth.signOut()
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No") { _, _ ->
                exitDialog.cancel()
            }
        exitDialog = builder.create()
    }

    private fun buttonBinding() {
        binding.closeBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            finish()
        }

        binding.logoutBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")

            exitDialog.show()
        }

        binding.homeBtn.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

        binding.leaderboardBtn.setOnClickListener {
            val intent = Intent(this, GameDifficulty::class.java)
            intent.putExtra("option", "leaderboard")
            startActivity(intent)
            finish()
        }
    }

    private fun soundBinding() {
        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        // Change volume from seekbar values
        val savedMusicVolume = sharedPreferences.getInt("musicVolume", 50)
        val savedSfxVolume = sharedPreferences.getInt("sfxVolume", 50)

        binding.musicBar.progress = savedMusicVolume
        binding.sfxBar.progress = savedSfxVolume

        binding.musicBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val volume = progress / 100.0f
                SoundManager.musicVolume = volume
                SoundManager.mediaPlayer?.setVolume(volume, volume) // Adjust current song volume
                saveVolumeSetting(sharedPreferences, "musicVolume", progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.sfxBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val volume = progress / 100.0f
                SoundManager.sfxVolume = volume
                saveVolumeSetting(sharedPreferences, "sfxVolume", progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.musicBtn.setOnClickListener {
            val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            SoundManager.isMusicEnabled = !SoundManager.isMusicEnabled
            editor.putBoolean("isMusicEnabled", SoundManager.isMusicEnabled)

            if (SoundManager.isMusicEnabled) {
                binding.musicBtn.setImageResource(R.drawable.ic_music)
                SoundManager.mediaPlayer?.start()
            } else {
                binding.musicBtn.setImageResource(R.drawable.ic_music_off)
                SoundManager.mediaPlayer?.pause()
            }
            editor.apply()
        }

        binding.sfxBtn.setOnClickListener {
            val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            SoundManager.isSfxEnabled = !SoundManager.isSfxEnabled
            editor.putBoolean("isSfxEnabled", SoundManager.isSfxEnabled)

            if (SoundManager.isSfxEnabled) {
                binding.sfxBtn.setImageResource(R.drawable.ic_sfx)
            } else {
                binding.sfxBtn.setImageResource(R.drawable.ic_sfx_off)
            }
            editor.apply()
        }

        // Sound Processing
        SoundManager.isMusicEnabled = sharedPreferences.getBoolean("isMusicEnabled", true)
        SoundManager.isSfxEnabled = sharedPreferences.getBoolean("isSfxEnabled", true)

        // Music on/off
        if (SoundManager.isMusicEnabled) {
            binding.musicBtn.setImageResource(R.drawable.ic_music)
        } else {
            binding.musicBtn.setImageResource(R.drawable.ic_music_off)
        }

        // SFX on/off
        if (SoundManager.isSfxEnabled) {
            binding.sfxBtn.setImageResource(R.drawable.ic_sfx)
        } else {
            binding.sfxBtn.setImageResource(R.drawable.ic_sfx_off)
        }
    }

    private fun saveVolumeSetting(sharedPreferences: SharedPreferences, key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    override fun onPause() {
        super.onPause()
        SoundManager.mediaPlayer?.pause() // Pause music when app is in the background
    }

    override fun onResume() {
        super.onResume()
        SoundManager.mediaPlayer?.start() // Resume music when app comes back to the foreground
    }
}