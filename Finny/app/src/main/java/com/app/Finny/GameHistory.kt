package com.app.Finny

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.UserController
import com.app.Finny.Models.History
import com.app.Finny.Models.UserModel
import com.app.Finny.databinding.ActivityGameHistoryBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GameHistory : AppCompatActivity() {
    private lateinit var binding: ActivityGameHistoryBinding
    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGameHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.exitBtn.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        val currentUser = auth.currentUser!!
        val uid = currentUser.uid
        val usrController = UserController()
        val channel = Channel<UserModel>()
        val user: UserModel
        GlobalScope.launch {
            val data = usrController.getOneById(uid)
            channel.send(data)
        }
        runBlocking {
            user = channel.receive()
        }

        val historyList: List<History> = user.history   // contain game history of the logged in user


//        val historyContainer: LinearLayout = binding.historyListContainer
//        for (history in historyList) {
//            // Create a new LinearLayout for each history item
//            val historyItemLayout = LinearLayout(this)
//            historyItemLayout.orientation = LinearLayout.HORIZONTAL
//            historyItemLayout.setPadding(0, 8, 0, 8)
//            historyItemLayout.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//
//            val dateTextView = TextView(this)
//            dateTextView.text = history.date
//            dateTextView.textSize = 16f
//            dateTextView.layoutParams = LinearLayout.LayoutParams(
//                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
//            )
//
//            val difficultyTextView = TextView(this)
//            difficultyTextView.text = history.difficulty
//            difficultyTextView.textSize = 16f
//            difficultyTextView.layoutParams = LinearLayout.LayoutParams(
//                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
//            )
//
//            val scoreTextView = TextView(this)
//            scoreTextView.text = "Score: ${history.score}"
//            scoreTextView.textSize = 16f
//            scoreTextView.layoutParams = LinearLayout.LayoutParams(
//                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
//            )
//
//            val timeTextView = TextView(this)
//            timeTextView.text = "Time: ${history.timeTaken} seconds"
//            timeTextView.textSize = 16f
//            timeTextView.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//
//            historyItemLayout.addView(dateTextView)
//            historyItemLayout.addView(difficultyTextView)
//            historyItemLayout.addView(scoreTextView)
//            historyItemLayout.addView(timeTextView)
//
//            historyContainer.addView(historyItemLayout)
//        }
    }
}