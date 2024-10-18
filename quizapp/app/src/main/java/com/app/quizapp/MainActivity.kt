package com.app.quizapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        printDocuments()
    }

    fun printDocuments() {
        // Create a firestore instance
        val db = FirebaseFirestore.getInstance()

        // Get a reference to the "account" collection
        val accountCol = db.collection("account")

        // Iterate through each document in the collection
        accountCol.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                println("\nID: ${document.id}")
                for (key in data.keys) {
                    println("$key: ${data[key]}")
                }
            }
        }
    }

}