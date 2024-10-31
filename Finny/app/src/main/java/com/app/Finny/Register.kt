package com.app.Finny

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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    // Create a firestore instance
    private val db = FirebaseFirestore.getInstance()
    // Get a reference to the "account" collection
    private lateinit var accountRef: DocumentReference

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
            val base_pass = binding.password.text.toString().trim()
            val check_pass = binding.passwordConfirm.text.toString().trim()

            val userC = UserController()
            auth.createUserWithEmailAndPassword(email, base_pass)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        println("User with email ${email} created successfully")
                        val user = auth.currentUser!!
                        userC.createOne(user.uid, user.email.toString(), name)
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                    } else {
                        println(task.exception)
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    }
                }

//            // Check if the user exists
//            val userC = UserController()
//            val userCheck = userC.getOneByEmail(email)
//
//            if(userCheck.uid == "blank") {
//                if(base_pass.length < 6) {
//                    Toast.makeText(
//                        baseContext,
//                        "Passwords need to be at least 6 characters",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    // Check if the passwords match
//                    if(base_pass == check_pass) {
//                        auth.createUserWithEmailAndPassword(email, base_pass)
//                            .addOnCompleteListener(this) { task ->
//                                if(task.isSuccessful) {
//                                    println("User with email ${email} created successfully")
//                                    val user = auth.currentUser!!
//                                    userC.createOne(user.uid, user.email.toString(), name)
//                                    val intent = Intent(this, Home::class.java)
//                                    startActivity(intent)
//                                } else {
//                                    println(task.exception)
//                                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                                }
//                            }
//
//                    } else {
//                        println("mismatch")
//                        Toast.makeText(
//                            baseContext,
//                            "Passwords don't match",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//            } else {
//                println("exist")
//                Toast.makeText(
//                    baseContext,
//                    "User already exist",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
        }
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