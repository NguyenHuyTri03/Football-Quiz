package com.app.Finny.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.databinding.ActivityLeaderboardDifficultiesBinding
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import com.app.Finny.SoundManager

class LeaderboardDifficulties : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardDifficultiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeaderboardDifficultiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.closeBtn.setOnClickListener {
            finish()
        }

        binding.easyBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            val intent = Intent(this, Leaderboard::class.java)
            intent.putExtra("difficulty", "easy")
            startActivity(intent)
        }
        binding.mediumBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            val intent = Intent(this, Leaderboard::class.java)
            intent.putExtra("difficulty", "medium")
            startActivity(intent)
        }
        binding.expertBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            val intent = Intent(this, Leaderboard::class.java)
            intent.putExtra("difficulty", "expert")
            startActivity(intent)
        }
    }

    private fun openLeaderboard(difficulty: String) {
        val intent = Intent(this, Leaderboard::class.java)
        intent.putExtra("difficulty", difficulty)
        startActivity(intent)
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