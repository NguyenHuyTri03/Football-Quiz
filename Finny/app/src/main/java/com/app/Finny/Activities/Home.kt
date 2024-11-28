package com.app.Finny.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.databinding.ActivityHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.getStringExtra("userName") != null) {
            binding.userName.text = intent.getStringExtra("userName").toString()
        } else {
            binding.userName.text = auth.currentUser!!.displayName
        }

        // button bindings
        binding.playBtn.setOnClickListener {
            val intent = Intent(this, GameDifficulty::class.java)
            startActivity(intent)
        }

        binding.historyBtn.setOnClickListener {
            val intent = Intent(this, GameHistory::class.java)
            startActivity(intent)
        }

        binding.settingsBtn.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        binding.leaderboardBtn.setOnClickListener {
            val intent = Intent(this, LeaderboardDifficulties::class.java)
            startActivity(intent)
        }
    }
}