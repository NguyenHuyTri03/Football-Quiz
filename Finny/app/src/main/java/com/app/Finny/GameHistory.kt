package com.app.Finny

import android.content.Intent
import android.os.Bundle
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

    }
}