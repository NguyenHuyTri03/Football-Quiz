package com.app.Finny.Controllers


import android.content.ContentValues.TAG
import android.util.Log
import com.app.Finny.Models.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User


class UserController {
    // Create a firestore instance
    private val db = FirebaseFirestore.getInstance()

    // Get a reference to the "account" collection
    private val accountCol = db.collection("account")

    fun getAll() {

    }

    fun getOneById(uid: String, callback: (res: UserModel) -> Unit) {
        var user = UserModel()

        db.collection("account").document(uid).get()
            .addOnSuccessListener { document ->
                val data = document.data!!

                user = UserModel(
                    uid,
                    data.get("name").toString(),
                    data.get("email").toString(),
                    data.get("score_easy").toString().toInt(),
                    data.get("score_medium").toString().toInt(),
                    data.get("score_expert").toString().toInt()
                )
            }

        callback.invoke(user)
    }

    fun getOneByEmail(email: String, callback: (res: UserModel) -> Unit){
        val userQuery = accountCol.whereEqualTo("email", email)
        var user = UserModel()

        userQuery.get()
            .addOnSuccessListener { snapshot ->
                if(!snapshot.isEmpty) {
                    val data = snapshot.documents[0].toObject(UserModel::class.java)!!

                    user = data
                } else {
                    user = UserModel("blank", "", "", 0, 0, 0)
                }
            }

        callback.invoke(user)
    }

    // Add the current user to DB if they don't exist
    fun createOne(uid: String, email: String, name: String) {
        val accountRef = db.collection("account").document(uid)

        accountRef.set(mapOf(
            "uid" to uid,
            "email" to email,
            "name" to name,
            "score_easy" to 0,
            "score_medium" to 0,
            "score_expert" to 0,
        ))
            .addOnSuccessListener {
                Log.d(TAG,"Added user")
            }
            .addOnFailureListener {
                Log.w(TAG, "User wasn't added")
            }
    }

    fun updateScore(uid: String, score:Int, difficulty: String) {
        var db_score = 0
        val user = UserModel()

        // get score from db
        getOneById(uid) { user ->
            if(difficulty == "easy") {
                db_score = user.score_easy
            } else if(difficulty == "medium") {
                db_score = user.score_medium
            } else {
                db_score = user.score_expert
            }
        }

        // if score from db < current score, update current score, else, do nothing
        if(db_score < score) {
            db.collection("account").document(uid)
                .update("score_${difficulty}",score)
                .addOnSuccessListener {
                    Log.d(TAG, "User {${uid}} score uploaded successfully")
                }
                .addOnFailureListener { error ->
                    Log.w(TAG, "User {${uid}} score failed to upload with error: ", error)
                }

            // check and upload score to leaderboard

        }
    }

    fun update(user: UserModel) {
//        // change user name
//        val email = user.email
//        val newName = "Cheo"
//
//        val oldname = user.displayName
//        println("new name: ${oldname}")
//
//        val profileUpdate = UserProfileChangeRequest.Builder()
//            .setDisplayName(newName)
//            .build()
//
//        user.updateProfile(profileUpdate)
//            .addOnCompleteListener { task ->
//                if(task.isSuccessful) {
//                    print("name changed")
//                } else {
//                    print("Name doesn't change")
//                }
//            }
    }

    fun delete(id: String) {

    }
}