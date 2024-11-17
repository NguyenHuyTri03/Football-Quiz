package com.app.Finny.Models

import java.io.Serializable

data class History (
    val id: String,
    val date: String,
    val difficulty: String,
    val score: Int,
    val timeTaken: Int
) : Serializable {
    constructor(): this("", "", "", 0, 0)
}