package com.app.Finny.Activities

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.SoundManager
import com.app.Finny.databinding.ActivityGameDifficultyBinding

class GameDifficulty : AppCompatActivity() {
    private lateinit var binding: ActivityGameDifficultyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGameDifficultyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val option = intent.getStringExtra("option").toString()

        if(option == "leaderboard") {
            leaderboardBinding()
        } else {
            playBinding()
        }

        binding.homeBtn.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

        binding.settingsBtn.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }

        binding.closeBtn.setOnClickListener{
            finish()
        }
    }

    private fun playBinding() {
        binding.title.text = "Difficulty"

        binding.easyBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            startGame("easy")
        }
        binding.mediumBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            startGame("medium")
        }
        binding.expertBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            startGame("expert")
        }
    }

    private fun leaderboardBinding() {
        binding.title.text = "Leaderboard"

        binding.easyBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            openLeaderboard("easy")
        }
        binding.mediumBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            openLeaderboard("medium")
        }
        binding.expertBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            openLeaderboard("expert")
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

    private fun openLeaderboard(difficulty: String) {
        val intent = Intent(this, Leaderboard::class.java)
        intent.putExtra("difficulty", difficulty)
        startActivity(intent)
    }

    private fun startGame(difficulty: String) {
        val intent = Intent(this, PlayGame::class.java)
        intent.putExtra("difficulty", difficulty)
        startActivity(intent)
    }
}