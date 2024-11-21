package com.app.Finny

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Models.Sheet
import com.app.Finny.databinding.ActivityAnswerSheetBinding
import kotlinx.serialization.json.Json

class AnswerSheet : AppCompatActivity() {
    private lateinit var binding: ActivityAnswerSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAnswerSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get data from Endgame Activity
        val data = intent.getStringExtra("sheet")!!
        val sheet = Json.decodeFromString<Sheet>(data)

        checkAnswers(sheet)
    }

    private fun checkAnswers(sheet: Sheet) {
        val answers = sheet.answers
        val valid = sheet.validAnswers
        val qList = sheet.questionList

        // bind questions to UI
        // change the correct answer button to "btn_easy"


        // check and change colors of the buttons
        for((i, question) in qList.withIndex()) {
            if(answers[i] != valid[i]) {    // the options chose is wrong
                // change the option bg to "btn_hard"

            }
        }


    }
}