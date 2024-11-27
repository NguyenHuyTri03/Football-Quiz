package com.app.Finny.Activities

import com.app.Finny.R
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.SoundManager
import com.app.Finny.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = Firebase.auth
        val currentUser = auth.currentUser!!
        val username = currentUser.displayName

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userName.text = username

        // button bindings
        binding.playBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            val intent = Intent(this, GameDifficulty::class.java)
            startActivity(intent)
        }

        binding.historyBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            val intent = Intent(this, GameHistory::class.java)
            startActivity(intent)
        }

        binding.settingsBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        binding.leaderboardBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            val intent = Intent(this, LeaderboardDifficulties::class.java)
            startActivity(intent)
        }


        SoundManager.initializeVolumes(this)
        // Play the main song
        if (SoundManager.mediaPlayer == null || SoundManager.mediaPlayer?.isPlaying == false) {
            SoundManager.playSong(this, R.raw.spring_day, loop = true)
        }
    }

    override fun onPause() {
        super.onPause()
        SoundManager.mediaPlayer?.pause() // Pause music when app is in the background
    }

    override fun onResume() {
        super.onResume()
        SoundManager.mediaPlayer?.start() // Resume music when app comes back to the foreground
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundManager.release()
    }
}