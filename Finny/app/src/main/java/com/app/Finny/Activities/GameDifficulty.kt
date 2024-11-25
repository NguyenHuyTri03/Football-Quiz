package com.app.Finny.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.databinding.ActivityGameDifficultyBinding

class GameDifficulty : AppCompatActivity() {
    private lateinit var binding: ActivityGameDifficultyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGameDifficultyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.easyBtn.setOnClickListener {
            val intent = Intent(this, PlayGame::class.java)
            intent.putExtra("difficulty", "easy")
            startActivity(intent)
        }

        binding.mediumBtn.setOnClickListener {
            val intent = Intent(this, PlayGame::class.java)
            intent.putExtra("difficulty", "medium")
            startActivity(intent)
        }

        binding.expertBtn.setOnClickListener {
            val intent = Intent(this, PlayGame::class.java)
            intent.putExtra("difficulty", "expert")
            startActivity(intent)
        }

        binding.backBtn.setOnClickListener{
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}