package com.app.Finny.Models

import java.io.Serializable

data class UserModel (
    val uid: String,
    val name: String,
    val email: String,
    val score_easy: Int,
    val score_medium: Int,
    val score_expert: Int,
    val history: List<History>
) : Serializable {
    constructor(): this("", "", "", 0, 0, 0, emptyList())
}