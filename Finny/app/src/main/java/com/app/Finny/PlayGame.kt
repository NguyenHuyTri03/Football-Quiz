package com.app.Finny

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Models.QuestionModel
import com.app.Finny.databinding.ActivityPlayGameBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random


class PlayGame : AppCompatActivity() {
    private lateinit var binding: ActivityPlayGameBinding
    private val db = FirebaseFirestore.getInstance()
    private var questionList = mutableListOf<QuestionModel>()
//    private var Drawable drawable =

    private var questionIndex = 0
    private var questionTime = 0
    private var scorePerQuestion = 0
    private var totalScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPlayGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get difficulty from Home Activity
        var gameDifficulty: String = intent.getStringExtra("difficulty").toString()
        // set the score and time according to the difficulty
        if(gameDifficulty == "easy") {
            questionTime = 50
            scorePerQuestion = 10
        } else if(gameDifficulty == "medium") {
            questionTime = 40
            scorePerQuestion = 20
        } else {
            questionTime = 30
            scorePerQuestion = 30
        }

        // start a thread to put on a loading screen while fetching data
        Thread {
            startActivity(Intent(this, SplashScreen::class.java))

            getAllByDifficulty(gameDifficulty) { qList ->
                val randomNumbers = List(5) { Random.nextInt(0, 20) }

                for(num in randomNumbers) {
                    questionList.add(qList[num])
                }

                putQuestion()
            }
        }.start()

        startTimer()

        binding.apply {
            option1.setOnClickListener {
                checkAnswer(option1.text.toString(), 1)
            }
            option2.setOnClickListener {
                checkAnswer(option2.text.toString(), 2)
            }
            option3.setOnClickListener {
                checkAnswer(option3.text.toString(), 3)
            }
            option4.setOnClickListener {
                checkAnswer(option4.text.toString(), 4)
            }
        }
    }

    private fun putQuestion() {
        binding.apply {
            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#949494")))
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#949494")))
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#949494")))
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#949494")))

            questionQuant.text = "Question ${questionIndex + 1}/5"
            questionTitle.text = questionList[questionIndex].question
            option1.text = questionList[questionIndex].options[0]
            option2.text = questionList[questionIndex].options[1]
            option3.text = questionList[questionIndex].options[2]
            option4.text = questionList[questionIndex].options[3]
        }
    }

    private fun checkAnswer(answer: String, op: Int) {
        if(answer == questionList[questionIndex].correct) {
            changeButtonColor(true, op)
            totalScore += scorePerQuestion
        } else {
            changeButtonColor(false, op)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            questionIndex++

            if(questionIndex < 5) {
                putQuestion()
            } else {
                //
            }
        }, 1000)
    }

    // change button color according to the answer
    private fun changeButtonColor(state: Boolean, op: Int) {
        if(state == true) {
            if(op == 1) {
                binding.option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94E161")))
            } else if(op == 2) {
                binding.option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94E161")))
            } else if(op == 3) {
                binding.option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94E161")))
            } else if(op == 4) {
                binding.option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94E161")))
            }
        } else {
            if(op == 1) {
                binding.option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F34D37")))
            } else if(op == 2) {
                binding.option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F34D37")))
            } else if(op == 3) {
                binding.option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F34D37")))
            } else if(op == 4) {
                binding.option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F34D37")))
            }
        }
    }

    private fun startTimer() {
        val totalTime = questionTime * 1000L
        object : CountDownTimer(totalTime,1000L){
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toDouble()
                binding.progressBar.progress = (100 - ((seconds/totalTime) * 100000)).toInt()
            }

            override fun onFinish() {
                //Finish the quiz
            }
        }.start()
    }

    fun getAllByDifficulty(difficulty: String, callback: (res: List<QuestionModel>) -> Unit) {
        var questions = mutableListOf<QuestionModel>()

        db.collection("${difficulty}_questions").get()
            .addOnSuccessListener { documents ->
                var i = 0
                for(document in documents) {
                    val data = document.data
                    val option_list: List<String> = data.get("options") as List<String>

                    val question = QuestionModel(document.id, data.get("image_url").toString(), data.get("question").toString(), option_list, data.get("correct").toString())
                    questions.add(question)
                }

                callback.invoke(questions)
            }
    }
}