package com.app.Finny.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.QuestionController
import com.app.Finny.R
import com.app.Finny.SoundManager
import com.app.Finny.databinding.ActivityHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.getStringExtra("userName") != null) {
            val text = "Hi ${intent.getStringExtra("userName")}"
            binding.greet.text = text
        } else {
            val text = "Hi ${auth.currentUser!!.displayName}"
            binding.greet.text = text
        }

        // button bindings
        binding.playBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")

            val intent = Intent(this, GameDifficulty::class.java)
            intent.putExtra("option", "play")
            startActivity(intent)
        }

        binding.historyBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")

            val intent = Intent(this, GameHistory::class.java)
            startActivity(intent)
        }

        binding.tutorialBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")

            val intent = Intent(this, Tutorial::class.java)
            startActivity(intent)
        }

        binding.settingsBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")

            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        binding.leaderboardBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")

            val intent = Intent(this, GameDifficulty::class.java)
            intent.putExtra("option", "leaderboard")
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