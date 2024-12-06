package com.app.Finny.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.QuestionController
import com.app.Finny.Models.QuestionModel
import com.app.Finny.databinding.ActivityQuestionFormBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class QuestionForm : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionFormBinding
    private val questionController = QuestionController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityQuestionFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id").toString()
        val difficulty = intent.getStringExtra("difficulty").toString()
        val channel = Channel<QuestionModel>()

        var question: QuestionModel

        GlobalScope.launch {
            val data = questionController.getOne(difficulty, id)

            channel.send(data)
        }
        runBlocking {
            question = channel.receive()
        }

        binding.apply {
            title.text = question.id

            questionTextInput.hint = question.question
            optionInput1.hint = question.options[0]
            optionInput2.hint = question.options[1]
            optionInput3.hint = question.options[2]
            correctInput.hint = question.correct
//            imgUrlInput.isSingleLine = true
//            imgUrlInput.hint = question.image_url
        }

        binding.deleteBtn.setOnClickListener {
            questionController.delete(difficulty, id)
            startActivity(Intent(this, AdminQuestions::class.java))
            finish()
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun formatData() {

    }
}