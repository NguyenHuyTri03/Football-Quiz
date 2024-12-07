package com.app.Finny.Activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.QuestionController
import com.app.Finny.Models.QuestionModel
import com.app.Finny.R
import com.app.Finny.databinding.ActivityQuestionAddFormBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.concurrent.Executors
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base64
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast

class QuestionAddForm : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionAddFormBinding
    private var questionController = QuestionController()
    private var difficulty = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityQuestionAddFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var lastId = 0
        var id = ""
        val diff = mutableListOf<String>()
        diff.add("easy")
        diff.add("medium")
        diff.add("expert")

        // Create a dropdown to select difficulty
        val autoCompleteTextView: AutoCompleteTextView = binding.autocompleteText
        var optionAdapter = ArrayAdapter(this, R.layout.admin_question_diff_select, diff)

        autoCompleteTextView.setAdapter(optionAdapter)
        autoCompleteTextView.setOnItemClickListener { diffView, _, pos, _ ->
            val diffAtPos = diffView.getItemAtPosition(pos).toString()
            difficulty = diffAtPos

            val channel = Channel<Int>()
            GlobalScope.launch {
                val data = questionController.getAllByDifficulty(diffAtPos)
                val id = data[data.lastIndex].id

                channel.send(id.split("_")[1].toInt())
            }
            runBlocking {
                lastId = channel.receive()
            }

            id = if(difficulty == "medium") {
                "norm_${lastId + 1}"
            } else {
                "${difficulty}_${lastId + 1}"
            }
        }

        var options = mutableListOf<String>()
        var questionText: String
        var correct: String
        var imgUrl: String
        var processedUrl: String
        var option1: String
        var option2: String
        var option3 : String



        binding.addBtn.setOnClickListener {
            options = mutableListOf<String>()
            questionText = binding.questionTextInput.text.toString()
            correct = binding.correctInput.text.toString()
            imgUrl = binding.imgUrlInput.text.toString()
            processedUrl = ""
            option1 = binding.optionInput1.text.toString()
            option2 = binding.optionInput2.text.toString()
            option3 = binding.optionInput3.text.toString()
            options.add(option1)
            options.add(option2)
            options.add(option3)
            options.add(correct)

            if(imgUrl != "") {
                processedUrl = processImgUrl(imgUrl.toString())
            }

            if(questionText != "" && option1 != ""
                && option2 != "" && option3 != ""
                && correct != "") {
                val question = QuestionModel(
                    id,
                    processedUrl,
                    questionText,
                    options,
                    correct
                )

                val msg = Toast.makeText(
                    this,
                    "Question added",
                    Toast.LENGTH_SHORT
                )

                runBlocking {
                    questionController.createOne(difficulty, question)
                    msg.show()
                }
                finish()
            }
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun processImgUrl(url: String): String {
        var image:Bitmap?
        val executor = Executors.newSingleThreadExecutor()
        var bitmap = ""

        executor.execute {
            val `in` = URL(url).openStream()
            image = BitmapFactory.decodeStream(`in`)

             bitmap = encodeImage(image)!!
        }

        return bitmap
    }

    private fun encodeImage(bm: Bitmap?): String? {
        val baos = ByteArrayOutputStream()
        bm?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeBase64String(b)
    }
}