@file:OptIn(DelicateCoroutinesApi::class)

package com.app.Finny.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.UserController
import com.app.Finny.Models.UserModel
import com.app.Finny.SoundManager
import com.app.Finny.databinding.ActivityEndGameBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EndGame : AppCompatActivity() {
    private lateinit var binding: ActivityEndGameBinding
    private lateinit var scores: IntArray
    private var auth = Firebase.auth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEndGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = auth.currentUser!!
        val uid = user.uid

        scores = intent.getIntArrayExtra("scores")!!
        val difficulty = intent.getStringExtra("difficulty")!!
//        val data = intent.getStringExtra("sheet")!!
//        val sheet = Json.decodeFromString<Sheet>(data)
//        // get data from Endgame Activity
//        val data = intent.getStringExtra("sheet")!!
//        val sheet = Json.decodeFromString<Sheet>(data)
//        intent.putExtra("sheet", Json.encodeToString(sheet))

//      endGameVals = intArrayOf(correctAnswers, timeTaken, timeBonus, finalScore)
        val correct = "${scores[0]}/5"
        val timeTaken: Int = scores[1]
        val bonus: Int = scores[2]
        val finalScore: Int = scores[3]

        binding.apply {
            score.text = finalScore.toString()
            valueTime.text = timeTaken.toString()
            valueCorrect.text = correct
            valueBonus.text = bonus.toString()
        }

        binding.retryBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            val intent = Intent(this, PlayGame::class.java)
            intent.putExtra("difficulty", difficulty)
            startActivity(intent)
//            updateUserScore(uid, )
            finish()
        }

        binding.homeBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")
            runBlocking {
                updateUserScore(uid, finalScore, difficulty, timeTaken)
            }

            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

        // Stop the in-game song and play the "complete" SFX
        SoundManager.stopSong()
        SoundManager.playSFX(this, "complete")
    }

    private suspend fun updateUserScore(uid: String, score: Int, difficulty: String, timeTaken: Int) {
        val usrController = UserController()
        val user: UserModel

        val channel = Channel<UserModel>()
        GlobalScope.launch {
            val data = usrController.getOneById(uid)
            channel.send(data)
        }

        user = channel.receive()

        val dbScore: Int = when (difficulty) {
            "easy" -> {
                user.score_easy
            }

            "medium" -> {
                user.score_medium
            }

            else -> {
                user.score_expert
            }
        }

        // add score to user history
        usrController.addGameToHistory(score, timeTaken, difficulty)

        if(dbScore < score) {
            usrController.updateScore(score, difficulty)
        }
    }
}