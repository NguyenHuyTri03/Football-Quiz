package com.app.Finny

import android.content.Intent
import kotlin.random.Random
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.QuestionController
import com.app.Finny.Models.QuestionModel
import com.app.Finny.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding= ActivityHomeBinding.inflate(layoutInflater)
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

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}