package com.app.Finny

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.databinding.ActivityLeaderboardDifficultiesBinding
import android.content.Intent
import androidx.activity.enableEdgeToEdge

class LeaderboardDifficulties : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardDifficultiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeaderboardDifficultiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            closeButton.setOnClickListener {
                finish()
            }

            easyBtn.setOnClickListener {
                openLeaderboard("easy")
            }
            mediumBtn.setOnClickListener {
                openLeaderboard("medium")
            }
            expertBtn.setOnClickListener {
                openLeaderboard("expert")
            }
        }
    }

    private fun openLeaderboard(difficulty: String) {
        val intent = Intent(this, Leaderboard::class.java)
        intent.putExtra("difficulty", difficulty)
        startActivity(intent)
    }
}