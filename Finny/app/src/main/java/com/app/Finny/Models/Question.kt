package com.app.Finny.Models

import kotlinx.serialization.Serializable

@Serializable
data class QuestionModel (
    val id: String,
    val image_url: String,
    val question: String,
    var options: List<String>,
    val correct: String
) {
    constructor(): this("", "", "", emptyList(), "")
}