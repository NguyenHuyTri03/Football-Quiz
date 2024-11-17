package com.app.Finny.Controllers


import android.content.ContentValues.TAG
import android.util.Log
import com.app.Finny.Models.History
import com.app.Finny.Models.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class UserController {
    // Create a firestore instance
    private val db = FirebaseFirestore.getInstance()

    private var auth: FirebaseAuth = Firebase.auth

    // Get a reference to the "account" collection
    private val accountCol = db.collection("account")

    private val uid = auth.currentUser?.uid.toString()

    fun getAll() {

    }

    suspend fun getOneById(uid: String): UserModel {
        var user: UserModel

        val data = accountCol.document(uid).get().await()
        user = UserModel(
            uid,
            data.get("name").toString(),
            data.get("email").toString(),
            data.get("score_easy").toString().toInt(),
            data.get("score_medium").toString().toInt(),
            data.get("score_expert").toString().toInt()
        )

        return user
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
    fun createOne(id: String, email: String, name: String) {
        val accountRef = accountCol.document(id)

        accountRef.set(mapOf(
            "uid" to id,
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

    fun updateScore(score:Int, difficulty: String, timeTaken: Int) {
        accountCol.document(uid)
            .update("score_${difficulty}",score)
            .addOnSuccessListener {
                Log.d(TAG, "User {${uid}} score uploaded successfully")
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "User {${uid}} score failed to upload with error: ", error)
            }
    }

    fun addGameToHistory(score: Int, timeTaken: Int, difficulty: String) {
        val dateFormater = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val datetime = LocalDateTime.now().format(dateFormater)
        val idDate = datetime   // format the date as ddmmyyHHmm to go with uid
            .replace(" ", "").replace("/", "").replace(":", "")
        val id = difficulty + idDate

        val history = History(id, datetime, difficulty, score, timeTaken)
        accountCol.document(uid)
            .update("history", FieldValue.arrayUnion(history))
            .addOnSuccessListener {
                Log.d(TAG, "Updated game history successfully")
            }
            .addOnFailureListener {
                Log.w(TAG, "Error updating game history")
            }
    }

//    fun getGameHistory(uid: String, callback: (res: List<History>) -> Unit) {
//        var history: List<History>
//
//        val user =
//        history = user.history
//
//        callback.invoke(history)
//    }

    fun update(user: UserModel) {
    }

    fun delete(id: String) {

    }
}