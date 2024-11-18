package com.app.Finny.Models

import kotlinx.serialization.Serializable

@Serializable
data class Sheet (
    val answers: List<String>,
    val validAnswers: List<String>,
    val questionList: List<QuestionModel>
) {
    constructor(): this(emptyList(), emptyList(), emptyList())
}