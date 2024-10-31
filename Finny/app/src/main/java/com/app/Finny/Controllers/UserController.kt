package com.app.Finny.Controllers


import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


class UserController {

    // Create a firestore instance
    val db = FirebaseFirestore.getInstance()

    // Get a reference to the "account" collection
    val accountCol = db.collection("account")

    fun GetAccounts()  {

        accountCol.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data

//                users.add(data)
//                println("\nID: ${document.id}")
//                for (key in data.keys) {
//                    println("$key: ${data[key]}")
//                }
            }
        }
    }

    fun FindAccountById(id: String) {

        try {
            val docRef = accountCol.document(id)
            val document = runBlocking {
                docRef.get().await()
            }
        } catch(e: Exception) {
            println(e)
        }
    }

//    private fun MapData(document: Any): User {
//        var user = User()
//
//        user.id = document.id
//        user.name = document.getString("name")
//        user.email = document.getString("email")
//        user.password = document.getString("password")
//        user.easy_score = document.getLong("easy_score")
//        user.normal_score = document.getLong("easy_score")
//        user.hard_score = document.getLong("easy_score")
//    }
}