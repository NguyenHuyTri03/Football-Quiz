package com.app.Finny.Activities

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.closeBtn.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        SoundManager.isMusicEnabled = sharedPreferences.getBoolean("isMusicEnabled", true)
        SoundManager.isSfxEnabled = sharedPreferences.getBoolean("isSfxEnabled", true)

        //Chỗ này thêm giúp tui mô cái ảnh nào đó ở mục else để hiển thị nó bị disable nha
        if (SoundManager.isMusicEnabled) {
            binding.musicBtn.setImageResource(R.drawable.ic_music)
        } else {
            binding.musicBtn.setImageResource(R.drawable.ic_music)
        }

        //Chỗ này thêm giúp tui mô cái ảnh nào đó ở mục else để hiển thị nó bị disable nha
        if (SoundManager.isSfxEnabled) {
            binding.sfxBtn.setImageResource(R.drawable.ic_sfx)
        } else {
            binding.sfxBtn.setImageResource(R.drawable.ic_sfx)
        }

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

            //Chỗ này thêm giúp tui mô cái ảnh nào đó ở mục else để hiển thị nó bị disable nha
            if (SoundManager.isMusicEnabled) {
                binding.musicBtn.setImageResource(R.drawable.ic_music)
                SoundManager.mediaPlayer?.start()
            } else {
                binding.musicBtn.setImageResource(R.drawable.ic_music)
                SoundManager.mediaPlayer?.pause()
            }
            editor.apply()
        }

        binding.sfxBtn.setOnClickListener {
            val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            SoundManager.isSfxEnabled = !SoundManager.isSfxEnabled
            editor.putBoolean("isSfxEnabled", SoundManager.isSfxEnabled)

            //Chỗ này thêm giúp tui mô cái ảnh nào đó ở mục else để hiển thị nó bị disable nha
            if (SoundManager.isSfxEnabled) {
                binding.sfxBtn.setImageResource(R.drawable.ic_sfx)
            } else {
                binding.sfxBtn.setImageResource(R.drawable.ic_sfx)
            }
            editor.apply()
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