package com.app.Finny.Models

import java.io.Serializable

data class QuestionModel (
    val id: String,
    val image_url: String,
    val question: String,
    val options: List<String>,
    val correct: String
) : Serializable {
    constructor(): this("", "", "", emptyList(), "")
}