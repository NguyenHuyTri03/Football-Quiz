package com.app.Finny.Models

data class History (
    val id: String,
    val date: String,
    val difficulty: String,
    val score: Int,
    val timeTaken: Int
) {
    constructor(): this("", "", "", 0, 0)
}