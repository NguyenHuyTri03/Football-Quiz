package com.app.Finny.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.UserController
import com.app.Finny.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.runBlocking

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val userController = UserController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        auth = Firebase.auth

        binding.registerBtn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val name = binding.name.text.toString().trim()
            val basePass = binding.password.text.toString().trim()
            val confirmPass = binding.passwordConfirm.text.toString().trim()

            if(conditionCheck(name, basePass, confirmPass)) {
                auth.createUserWithEmailAndPassword(email, basePass)
                    .addOnSuccessListener {
                        // Create new user
                        val user = auth.currentUser!!
                        userController.createOne(user.uid, email, name)

                        val profileUpdates = userProfileChangeRequest {
                            displayName = name
                        }
                        user.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "User profile updated.")
                                }
                            }

                        val intent = Intent(this, Home::class.java)
                        intent.putExtra("userName", name)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Log.w(TAG, "createUserWithEmail:failure")
                        Toast.makeText(
                            this,
                            "User with the email is already exist",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    private fun conditionCheck(name: String, pass: String, confirmPass: String): Boolean {
        var nameStatus = false
        var passStatus = false

        runBlocking {
            val isAvailable = userController.checkName(name)

            if(!isAvailable && name.length <= 10) {
                nameStatus = true
            } else {
                makeErrorToast("User name already exist or longer than 15 characters")
            }
        }

        if(pass == confirmPass) {
            passStatus = true
        } else {
            makeErrorToast("Password mismatch")
        }

        return passStatus && nameStatus
    }

    private fun makeErrorToast(text: String) {
        Toast.makeText(
            baseContext,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}