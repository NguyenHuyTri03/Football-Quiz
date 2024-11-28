package com.app.Finny.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.R
import com.app.Finny.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val auth = Firebase.auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Checks for input and login with Auth
        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if(email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if(task.isSuccessful) {
                            Log.d(TAG, "Account {${email}} logs in successful")
                            val currUser = auth.currentUser
                            if(currUser != null && email != "admin@mail.com") {
                                val intent = Intent(this, Home::class.java)
                                startActivity(intent)
                            } else if(email == "admin@mail.com") {
                                // Login as admin
                                val intent = Intent(this, AdminHome::class.java)
                                startActivity(intent)
                            }
                            finish()
                        } else {
                            Log.w(TAG, "Account {${email}} failed to login")
                            Toast.makeText(
                                baseContext,
                                "Account fails to login ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    baseContext,
                    "Email and Password is required to login",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        var isVisible = false
        binding.viewPasswordBtn.setOnClickListener {
            if(!isVisible) {
                binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.viewPasswordBtn.setImageResource(R.drawable.ic_invisible)
                isVisible = true
            } else {
                binding.password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.viewPasswordBtn.setImageResource(R.drawable.ic_visible)
                isVisible = false
            }
            binding.password.setSelection(binding.password.text.length)
        }

        // Go to Register Activity
        binding.register.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    // Checks if user is already signed in and redirect them to the main menu
    public override fun onStart() {
        super.onStart()

        val currUser = auth.currentUser
        if(currUser != null) {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}