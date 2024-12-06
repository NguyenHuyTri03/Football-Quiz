package com.app.Finny.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Adapters.QuestionAdapter
import com.app.Finny.Controllers.QuestionController
import com.app.Finny.Models.QuestionModel
import com.app.Finny.R
import com.app.Finny.databinding.ActivityAdminQuestionsBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AdminQuestions : AppCompatActivity() {
    private lateinit var binding: ActivityAdminQuestionsBinding
    private val questionController = QuestionController()
    private var questionList = listOf<QuestionModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAdminQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.closeBtn.setOnClickListener {
            finish()
        }

        binding.addBtn.setOnClickListener {
            val intent = Intent(this, QuestionAddForm::class.java)
            startActivity(intent)
        }

        dropdownInit()
    }

    private fun dropdownInit() {
        val options = mutableListOf<String>()
        options.add("easy")
        options.add("medium")
        options.add("expert")

        val autoCompleteTextView: AutoCompleteTextView = binding.autocompleteText
        var optionAdapter = ArrayAdapter(this, R.layout.admin_question_diff_select, options)

        autoCompleteTextView.setAdapter(optionAdapter)
        autoCompleteTextView.setOnItemClickListener { diffView, _, pos, _ ->
            val diffAtPos = diffView.getItemAtPosition(pos).toString()

            val channel = Channel<List<QuestionModel>>()
            GlobalScope.launch {
                val data = questionController.getAllByDifficulty(diffAtPos)

                channel.send(data)
            }
            runBlocking {
                questionList = channel.receive()
            }

            var questionIDList = mutableListOf<String>()
            var questionTextList = mutableListOf<String>()

            questionList.forEach { question ->
                questionIDList.add(question.id)
                questionTextList.add(question.question)
            }

            val questionAdapter = QuestionAdapter(this, questionIDList, questionTextList)
            binding.list.adapter = questionAdapter
            binding.list.setOnItemClickListener { adapterView, _, position, _ ->
                val questionId = adapterView.getItemAtPosition(position).toString()
                val intent = Intent(this, QuestionForm::class.java)
                intent.putExtra("difficulty", diffAtPos)
                intent.putExtra("id", questionId)
                startActivity(intent)
            }

        }
    }
}