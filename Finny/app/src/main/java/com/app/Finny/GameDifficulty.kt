package com.app.Finny

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.Finny.Controllers.QuestionController
import com.app.Finny.Models.QuestionModel
import com.app.Finny.databinding.ActivityGameDifficultyBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GameDifficulty : AppCompatActivity() {
    private lateinit var binding: ActivityGameDifficultyBinding
    private var auth = Firebase.auth

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
            val intel = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}