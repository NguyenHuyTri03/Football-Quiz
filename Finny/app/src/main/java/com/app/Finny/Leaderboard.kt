package com.app.Finny

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Leaderboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val closeButton = findViewById<ImageButton>(R.id.closeButton)
        val leaderboardList = findViewById<ListView>(R.id.leaderboardList)

        // Close Leaderboard and return to difficulties page
        closeButton.setOnClickListener {
            finish()
        }

        // Get the selected difficulty
        val difficulty = intent.getStringExtra("difficulty") ?: "easy"

        // Fetch top players for the selected difficulty
        fetchTopPlayers(difficulty) { players ->
            // Bind players to ListView
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                players.map { "${it.name}: ${it.score}" }
            )
            leaderboardList.adapter = adapter
        }
    }

    private fun fetchTopPlayers(difficulty: String, callback: (List<Player>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val difficultyField = when (difficulty) {
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
                    val name = doc.getString("name") ?: "Unknown"
                    val score = doc.getLong(difficultyField)?.toInt() ?: 0
                    players.add(Player(name, score))
                }
                callback(players)
            }
            .addOnFailureListener { exception ->
                Log.e("Leaderboard", "Error fetching top players: ", exception)
            }
    }
}

data class Player(val name: String, val score: Int)