package com.app.Finny.Controllers

import com.app.Finny.Models.QuestionModel
import com.google.firebase.firestore.FirebaseFirestore


class QuestionController {
    // Create a firestore instance
    private val db = FirebaseFirestore.getInstance()

    // Reference to the "account" collection
    private val questionCol = db.collection("question")

    fun getAll() {

    }

    fun getOne(id: String) {

    }

    fun update(question: QuestionModel) {

    }

    fun delete(id: String) {

    }





    /***
    // DEPRECATED , DO NOT RUN
    // Find all questions at each difficulty and put them in their respective difficulty collective
    fun filterQuestions() {
        questionCol.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data

                if(document.id.contains("easy")) {
                    val question_list = listOf<String>(data.get("wrong_1").toString(), data.get("wrong_2").toString(), data.get("wrong_3").toString(), data.get("correct").toString())

                    val question: QuestionModel = QuestionModel(
                        document.id,
                        "",
                        data.get("text").toString(),
                        question_list,
                        data.get("correct").toString()
                    )

                    val quesRef = db.collection("easy_questions").document(document.id)
                    quesRef.set(question)

                } else if(document.id.contains("norm")) {
                    val question_list = listOf<String>(data.get("wrong_1").toString(), data.get("wrong_2").toString(), data.get("wrong_3").toString(), data.get("correct").toString())

                    val question: QuestionModel = QuestionModel(
                        document.id,
                        "",
                        data.get("text").toString(),
                        question_list,
                        data.get("correct").toString()
                    )

                    val quesRef = db.collection("medium_questions").document(document.id)
                    quesRef.set(question)

                } else if(document.id.contains("hard")) {
                    val question_list = listOf<String>(data.get("wrong_1").toString(), data.get("wrong_2").toString(), data.get("wrong_3").toString(), data.get("correct").toString())

                    val question: QuestionModel = QuestionModel(
                        document.id,
                        "",
                        data.get("text").toString(),
                        question_list,
                        data.get("correct").toString()
                    )

                    val quesRef = db.collection("expert_questions").document(document.id)
                    quesRef.set(question)
                }
            }
        }
    }

    ***/
}