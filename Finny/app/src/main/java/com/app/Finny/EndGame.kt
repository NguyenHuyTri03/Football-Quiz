package com.app.Finny

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.UserController
import com.app.Finny.Models.UserModel
import com.app.Finny.databinding.ActivityEndGameBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EndGame : AppCompatActivity() {
    private lateinit var binding: ActivityEndGameBinding
    private lateinit var scores: IntArray
    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEndGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = auth.currentUser!!
        val uid = user.uid

        scores = intent.getIntArrayExtra("scores")!!
        var difficulty = intent.getStringExtra("difficulty")!!

//      endGameVals = intArrayOf(correctAnswers, timeTaken, timeBonus, finalScore)
        var correct = "${scores[0]}/5"
        var timeTaken: Int = scores[1]
        var bonus: Int = scores[2]
        var finalScore: Int = scores[3]

        binding.apply {
            score.text = finalScore.toString()
            valueTime.text = timeTaken.toString()
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
            runBlocking {
                updateUserScore(uid, finalScore, difficulty, timeTaken)
            }

            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }
    }

    private suspend fun updateUserScore(uid: String, score: Int, difficulty: String, timeTaken: Int) {
        val userC = UserController()
        val user: UserModel
        var db_score = 0

        val channel = Channel<UserModel>()
        GlobalScope.launch {
            val data = userC.getOneById(uid)
            channel.send(data)
        }

        user = channel.receive()

        if(difficulty == "easy") {
            db_score = user.score_easy
        } else if(difficulty == "medium") {
            db_score = user.score_medium
        } else {
            db_score = user.score_expert
        }

        if(db_score < score) {
            userC.updateScore(score, difficulty, timeTaken)
        }
    }
}