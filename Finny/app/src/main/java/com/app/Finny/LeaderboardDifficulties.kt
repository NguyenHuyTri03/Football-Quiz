package com.app.Finny

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.app.Finny.databinding.ActivityLeaderboardDifficultiesBinding
import android.widget.ImageButton
import android.widget.Button
import android.content.Intent

class LeaderboardDifficulties : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard_difficulties)

        val closeButton = findViewById<ImageButton>(R.id.closeButton)
        val easyBtn = findViewById<Button>(R.id.easyBtn)
        val mediumBtn = findViewById<Button>(R.id.mediumBtn)
        val expertBtn = findViewById<Button>(R.id.expertBtn)

        // Navigate back to the Home activity
        closeButton.setOnClickListener {
            finish()
        }

        // Navigate to Leaderboard with selected difficulty
        easyBtn.setOnClickListener { openLeaderboard("easy") }
        mediumBtn.setOnClickListener { openLeaderboard("medium") }
        expertBtn.setOnClickListener { openLeaderboard("expert") }
    }

    private fun openLeaderboard(difficulty: String) {
        val intent = Intent(this, Leaderboard::class.java)
        intent.putExtra("difficulty", difficulty)
        startActivity(intent)
    }
}