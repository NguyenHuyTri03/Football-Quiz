package com.app.quizapp.Facades

import com.app.quizapp.Models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


class UserFacade {

    // Create a firestore instance
    val db = FirebaseFirestore.getInstance()

    // Get a reference to the "account" collection
    val accountCol = db.collection("account")

    fun GetAccounts() {
        var users = List(20) { User() }

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

    fun FindAccountById(id: String): User? {
        var user = User()

        try {
            val docRef = accountCol.document(id)
            val document = runBlocking {
                docRef.get().await()
            }

            user.id = document.id
            user.name = document.getString("name")
            user.email = document.getString("email")
            user.password = document.getString("password")
            user.easy_score = document.getLong("easy_score")
            user.normal_score = document.getLong("easy_score")
            user.hard_score = document.getLong("easy_score")
        } catch(e: Exception) {
            println(e)
        }

        if(user != null) {
            return user
        } else {
            return null
        }
    }
}