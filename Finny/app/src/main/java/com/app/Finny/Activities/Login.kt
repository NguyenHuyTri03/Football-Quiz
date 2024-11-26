package com.app.Finny.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.UserController
import com.app.Finny.SoundManager
import com.app.Finny.databinding.ActivityLoginBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    // Create a firestore instance
    private val db = FirebaseFirestore.getInstance()
    // Get a reference to the "account" collection
    private lateinit var accountRef: DocumentReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

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
                            println("UID: ${currUser?.uid}")
                            val usrController = UserController()
                            if(currUser != null) {
                                // Reference to account collection and to the document of the current user
                                accountRef = db.collection("account").document(currUser.uid)

                                // Find if the logged in user exists and add them to db if not found
                                accountRef.get()
                                    .addOnSuccessListener { document ->
                                        val data = document.data

                                        if(data == null) {
                                            usrController.createOne(currUser.uid, currUser.email.toString(), currUser.displayName.toString())
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        println("Error: $exception")
                                    }

                                val intent = Intent(this, Home::class.java)
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
        println("UID: ${currUser?.uid}")
        val user = UserController()
        if(currUser != null) {
            // Reference to account collection and to the document of the current user
            accountRef = db.collection("account").document(currUser.uid)

            // Find if the logged in user exists and add them to db if not found
            accountRef.get()
                .addOnSuccessListener { document ->
                    val data = document.data

                    if(data == null) {
                        user.createOne(currUser.uid, currUser.email.toString(), currUser.displayName.toString())
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error: $exception")
                }

            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}