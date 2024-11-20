package com.app.Finny.Controllers

import com.app.Finny.Models.QuestionModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await


class QuestionController {
    // Create a firestore instance
    private val db = FirebaseFirestore.getInstance()

    suspend fun getAllByDifficulty(difficulty: String): List<QuestionModel> {
        var questions = listOf<QuestionModel>()

        val documents = db.collection("${difficulty}_questions").get().await()

        questions = documents.toObjects<QuestionModel>()

        return questions
    }

    fun getOne(id: String) {

    }

    fun update(question: QuestionModel) {

    }

    fun delete(id: String) {

    }





    /*** DEPRECATED , DO NOT RUN
     *

    // callback example
    fun getAllByDifficulty(difficulty: String, callback: (res: List<QuestionModel>) -> Unit) {
        var questions = mutableListOf<QuestionModel>()

        db.collection("${difficulty}_questions").get()
            .addOnSuccessListener { documents ->
            var i = 0
            for(document in documents) {
                val data = document.data
                val option_list: List<String> = data.get("options") as List<String>

                val question = QuestionModel(document.id, data.get("image_url").toString(), data.get("question").toString(), option_list, data.get("correct").toString())
                questions.add(question)
            }

            callback.invoke(questions)
        }
    }
    ***/
}