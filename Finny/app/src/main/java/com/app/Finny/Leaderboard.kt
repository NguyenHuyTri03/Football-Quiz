package com.app.Finny

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.UserController
import com.app.Finny.Models.UserModel
import com.app.Finny.databinding.ActivityLeaderboardBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

class Leaderboard : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.closeButton.setOnClickListener {
            finish()
        }

        val leaderboardList = binding.leaderboardList

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