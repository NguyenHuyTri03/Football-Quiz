package com.app.Finny

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.UserController
import com.app.Finny.databinding.ActivityEndGameBinding

class EndGame : AppCompatActivity() {
    private lateinit var binding: ActivityEndGameBinding
    private lateinit var scores: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEndGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            updateUserScore(finalScore, difficulty, timeTaken)

            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateUserScore(score: Int, difficulty: String, timeTaken: Int) {
        val userC = UserController()
        var db_score = 0

        userC.getOneById() { user ->
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
}