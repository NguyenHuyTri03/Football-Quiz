package com.app.Finny

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Models.UserModel
import com.app.Finny.databinding.ActivityLoginBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.ktx.firestore


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



                            val intent = Intent(this, Home::class.java)
                            startActivity(intent)
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

    // Checks if user is already signed in
    public override fun onStart() {
        super.onStart()

        val user = auth.currentUser
        if(user != null) {
            accountRef = db.collection("account").document(user.uid)

            // Find if the logged in user exists and add them to db if not found
            accountRef.get()
                .addOnSuccessListener { document ->
                    val data = document.data

                    if(data != null) {
                        println("user: ${data}")
                    } else {
                        addUserToDb(user.uid, user.email.toString(), user.displayName.toString())
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error: ${exception}")
                }

            val intent = Intent(this, Home::class.java)
            startActivity(intent)


            // change user name
            /*
            val email = user.email
            val newName = "Cheo"

            val oldname = user.displayName
            println("new name: ${oldname}")

            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build()

            user.updateProfile(profileUpdate)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        print("name changed")
                    } else {
                        print("Name doesn't change")
                    }
                }

             */
        }
    }

    private fun addUserToDb(uid: String, email: String, name: String) {
        accountRef.set(mapOf(
            "uid" to uid,
            "email" to email,
            "name" to name,
            "score_easy" to 0,
            "score_medium" to 0,
            "score_hard" to 0,
        ))
            .addOnSuccessListener {
                Log.d(TAG,"Added user")
            }
            .addOnFailureListener {
                Log.w(TAG, "User wasn't added")
            }
    }

    private fun getQuestions() {

    }
}