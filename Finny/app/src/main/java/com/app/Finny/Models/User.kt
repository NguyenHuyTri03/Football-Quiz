package com.app.Finny.Models

data class UserModel (
    val id: String,
    val name: String,
    val email: String,
    val score_easy: Int,
    val score_medium: Int,
    val score_expert: Int,
) {
    constructor(): this("", "", "", 0, 0, 0)
}