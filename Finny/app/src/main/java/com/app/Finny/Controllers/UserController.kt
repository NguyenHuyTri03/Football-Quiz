package com.app.Finny.Controllers


import android.content.ContentValues.TAG
import android.util.Log
import com.app.Finny.Models.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class UserController {
    // Create a firestore instance
    private val db = FirebaseFirestore.getInstance()

    // Get a reference to the "account" collection
    private val accountCol = db.collection("account")

    fun getAll() {

    }

    fun getOneById(id: String) {

    }

    fun getOneByEmail(email: String): UserModel {
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

        return user
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
        val updatedData = hashMapOf("score_${difficulty}" to score)

        db.collection("account").document(uid)
            .update("score_${difficulty}",updatedData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "User {${uid}} score uploaded successfully")
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "User {${uid}} score failed to upload with error: ", error)
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