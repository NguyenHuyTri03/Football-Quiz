package com.app.quizapp.Facades

import com.google.firebase.firestore.FirebaseFirestore

class dbContext {

    // Create a firestore instance
    val db = FirebaseFirestore.getInstance()

    // Get a reference to the "account" collection
    val questionCol = db.collection("question")

    // Get a reference to the "account" collection
    val quizSessionCol = db.collection("account")


}