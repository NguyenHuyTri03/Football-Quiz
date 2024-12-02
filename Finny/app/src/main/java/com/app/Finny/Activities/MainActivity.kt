package com.app.Finny.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.QuestionController
import com.app.Finny.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = Firebase.auth

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        binding.registerBtn.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        val mainIntent = Intent(this, Home::class.java)
        val adminIntent = Intent(this, AdminHome::class.java)

        if(auth.currentUser?.uid != null && auth.currentUser?.email != "admin@mail.com") {
            startActivity(mainIntent)
        } else if(auth.currentUser?.email == "admin@mail.com") {
            startActivity(adminIntent)
        }
    }
}