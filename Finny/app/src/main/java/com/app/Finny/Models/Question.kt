package com.app.Finny.Models

data class QuestionModel (
    val id: String,
    val image_url: String,
    val question: String,
    val options: List<String>,
    val correct: String
) {
    constructor(): this("", "", "", emptyList(), "")
}