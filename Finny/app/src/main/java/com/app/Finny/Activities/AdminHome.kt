package com.app.Finny.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.databinding.ActivityAdminHomeBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@DelicateCoroutinesApi
class AdminHome : AppCompatActivity() {
    private lateinit var binding: ActivityAdminHomeBinding
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userBtn.setOnClickListener {
            startActivity(Intent(this, AdminUsers::class.java))
        }

        binding.questionBtn.setOnClickListener {
            startActivity(Intent(this, AdminQuestions::class.java))
        }

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}