@file:OptIn(DelicateCoroutinesApi::class)

package com.app.Finny.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Adapters.HistoryAdapter
import com.app.Finny.Controllers.UserController
import com.app.Finny.Models.History
import com.app.Finny.Models.UserModel
import com.app.Finny.databinding.ActivityGameHistoryBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
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

        // start a thread to put on a loading screen while fetching data
        val splashIntent = Intent(this, SplashScreen::class.java)
        startActivity(splashIntent)

        setContentView(binding.root)

        binding.closeBtn.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        val currentUser = auth.currentUser!!
        val uid = currentUser.uid
        val userController = UserController()
        val channel = Channel<UserModel>()
        val user: UserModel
        GlobalScope.launch {
            val data = userController.getOneById(uid)
            channel.send(data)
        }
        runBlocking { user = channel.receive() }

        val historyList: List<History> = user.history.toList()  // contain game history of the logged in user
        val dateList = mutableListOf<String>()
        val difficultyList = mutableListOf<String>()
        val scoreList = mutableListOf<Int>()

        historyList.forEach { history ->
            dateList.add(history.date.split(" ")[0])
            difficultyList.add(history.difficulty)
            scoreList.add(history.score)
        }

        val historyAdapter = HistoryAdapter(this, dateList, difficultyList, scoreList)
        binding.list.adapter = historyAdapter
    }
}