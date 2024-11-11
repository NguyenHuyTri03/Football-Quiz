package com.app.Finny

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.UserController
import com.app.Finny.databinding.ActivityEndGameBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.app.Finny.Models.UserModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class EndGame : AppCompatActivity() {
    private lateinit var binding: ActivityEndGameBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var scores: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEndGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        scores = intent.getIntArrayExtra("scores")!!
        var difficulty = intent.getStringExtra("difficulty")!!

//      endGameVals = intArrayOf(correctAnswers, timeTaken, timeBonus, finalScore)
        var correct = "${scores[0]}/5"
        var time: Int = scores[1]
        var bonus: Int = scores[2]
        var finalScore: Int = scores[3]

        binding.apply {
            score.text = finalScore.toString()
            valueTime.text = time.toString()
            valueCorrect.text = correct
            valueBonus.text = bonus.toString()
        }

        binding.retryBtn.setOnClickListener {
            val intent = Intent(this, PlayGame::class.java)
            intent.putExtra("difficulty", difficulty)
            startActivity(intent)
            finish()
        }

        binding.homeBtn.setOnClickListener {
            updateUserScore(finalScore, difficulty)

            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateUserScore(score: Int, difficulty: String) {
        val userC = UserController()
        val uid = auth.currentUser?.uid!!

        userC.updateScore(uid, score, difficulty)
    }
}