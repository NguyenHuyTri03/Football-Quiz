package com.app.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.quizapp.Facades.UserFacade

import com.app.quizapp.Models.User as User


class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var btn_start = findViewById<Button>(R.id.btn_start)
        btn_start.setOnClickListener {
            val intent = Intent(this, Difficulty::class.java)
            startActivity(intent)
        }
        var userF = UserFacade()

        var user: User ?= null
        user = userF.FindAccountById("ply_0001")

        println("User: ${user?.name}")

    }
}