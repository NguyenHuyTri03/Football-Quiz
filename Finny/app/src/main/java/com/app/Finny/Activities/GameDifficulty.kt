package com.app.Finny.Activities

import android.content.Intent
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

        binding.easyBtn.setOnClickListener {
            SoundManager.playSFX(this, "start_game")

            val intent = Intent(this, PlayGame::class.java)
            intent.putExtra("difficulty", "easy")
            startActivity(intent)
        }

        binding.mediumBtn.setOnClickListener {
            SoundManager.playSFX(this, "start_game")

            val intent = Intent(this, PlayGame::class.java)
            intent.putExtra("difficulty", "medium")
            startActivity(intent)
        }

        binding.expertBtn.setOnClickListener {
            SoundManager.playSFX(this, "start_game")

            val intent = Intent(this, PlayGame::class.java)
            intent.putExtra("difficulty", "expert")
            startActivity(intent)
        }

        binding.closeBtn.setOnClickListener{
            finish()
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
}