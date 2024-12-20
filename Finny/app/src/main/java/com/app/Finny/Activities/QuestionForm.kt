package com.app.Finny.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
    private lateinit var deleteDialog: AlertDialog
    private lateinit var updateDialog: AlertDialog

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
//            imgUrlInput.hint = question.image_url
        }

        // Create a an delete confirmation popup
        val delBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        delBuilder
            .setTitle("Delete game?")
            // Return the admin to the question list
            .setPositiveButton("Yes") { _, _ ->
                questionController.delete(difficulty, id)
                startActivity(Intent(this, AdminQuestions::class.java))

                Toast.makeText(
                    this,
                    "Question deleted",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
            .setNegativeButton("No") { _, _ ->
                deleteDialog.cancel()
            }
        deleteDialog = delBuilder.create()

        // Create a an update confirmation popup
        val updateBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        updateBuilder
            .setTitle("Update question?")
            // Return the user to the question list
            .setPositiveButton("Yes") { _, _ ->
                questionController.update(difficulty, getDataFromInput(id, question))
                startActivity(Intent(this, AdminQuestions::class.java))

                Toast.makeText(
                    this,
                    "Question updated",
                    Toast.LENGTH_SHORT
                ).show()

//                finish()
            }
            .setNegativeButton("No") { _, _ ->
                updateDialog.cancel()
            }
        updateDialog = updateBuilder.create()

        binding.deleteBtn.setOnClickListener {
            deleteDialog.show()
        }

        binding.updateBtn.setOnClickListener {
            updateDialog.show()
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun getDataFromInput(id: String, currentQuestion: QuestionModel): QuestionModel {
        // Get new data to update
        var newQuestionText = binding.questionTextInput.text.toString()
        var newImageUrl = binding.imgUrlInput.text.toString()
        var newCorrect = binding.correctInput.text.toString()
        var newOptions = mutableListOf<String>()
        newOptions.add(binding.optionInput1.text.toString())
        newOptions.add(binding.optionInput2.text.toString())
        newOptions.add(binding.optionInput3.text.toString())
        newOptions.add(binding.correctInput.text.toString())

        if(newQuestionText == "") {
            newQuestionText = currentQuestion.question
        }
        if(newImageUrl == "") {
            newImageUrl = currentQuestion.image_url
        }
        if(newCorrect == "") {
            newCorrect = currentQuestion.correct
        }
        newOptions.forEach { option ->
            if(option == "") {
                newOptions[newOptions.indexOf(option)] = currentQuestion.options[newOptions.indexOf(option)]
            }
        }

        val question = QuestionModel(
            id,
            newImageUrl,
            newQuestionText,
            newOptions,
            newCorrect
        )

        return question
    }
}