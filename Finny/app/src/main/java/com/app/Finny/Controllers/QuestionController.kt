package com.app.Finny.Controllers

import com.google.firebase.firestore.FirebaseFirestore


class QuestionController {
    // Create a firestore instance
    val db = FirebaseFirestore.getInstance()

    // Reference to the "account" collection
    val questionCol = db.collection("question")


}