package com.app.Finny.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Adapters.LeaderboardAdapter
import com.app.Finny.SoundManager
import com.app.Finny.databinding.ActivityLeaderboardBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.io.Serializable

class Leaderboard : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // start loading screen
        val splashScreenIntent = Intent(this, SplashScreen::class.java)
        startActivity(splashScreenIntent)

        // Get the selected difficulty
        val difficulty = intent.getStringExtra("difficulty").toString()

        // Fetch top players for the selected difficulty
        fetchTopPlayers(difficulty) { players ->
            val name = mutableListOf<String>()
            val score = mutableListOf<Int>()

            players.forEach { player ->
                name.add(player.name)
                score.add(player.score)
            }

            val leaderboardAdapter = LeaderboardAdapter(this, name, score)
            binding.leaderboardList.adapter = leaderboardAdapter
        }

        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun fetchTopPlayers(difficulty: String, callback: (List<Player>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val difficultyField = when(difficulty) {
            "easy" -> "score_easy"
            "medium" -> "score_medium"
            "expert" -> "score_expert"
            else -> "score_easy"
        }

        db.collection("account")
            .orderBy(difficultyField, Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                val players = mutableListOf<Player>()
                for (doc in documents) {
                    val name = doc.getString("name").toString()
                    val score = doc.getLong(difficultyField)!!.toInt()
                    players.add(Player(name, score))
                }
                callback(players)
            }
            .addOnFailureListener { exception ->
                Log.e("Leaderboard", "Error fetching top players: ", exception)
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

data class Player(
    val name: String,
    val score: Int
) : Serializable {
    constructor(): this("Unknown", 0)
}