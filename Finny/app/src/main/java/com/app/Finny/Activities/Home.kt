package com.app.Finny.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.databinding.ActivityHomeBinding


class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userName.text = intent.getStringExtra("userName").toString()

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