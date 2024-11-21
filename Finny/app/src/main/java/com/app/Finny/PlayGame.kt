package com.app.Finny

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Controllers.QuestionController
import com.app.Finny.Models.QuestionModel
import com.app.Finny.databinding.ActivityPlayGameBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random
import kotlinx.serialization.Serializable


class PlayGame : AppCompatActivity() {
    private lateinit var binding: ActivityPlayGameBinding
    private var questionList = mutableListOf<QuestionModel>()
    private lateinit var gameDifficulty: String
    private lateinit var exitDialog: AlertDialog

    private val questionTime = 60
    private val scorePerQuestion = 10
    private var bonusPerSecond = 1
    private var questionIndex = 0
    private var timeTaken = 0
    private var totalScore = 0
    private var answers = mutableListOf<String>()
    private var validAnswers = mutableListOf<String>()

    // timer
    private val totalTime = questionTime * 1000L
    private var timer = object : CountDownTimer(totalTime,1000L){
        override fun onTick(millisUntilFinished: Long) {
            val seconds = (millisUntilFinished / 1000).toDouble()
            timeTaken++
            binding.progressBar.progress = (100 - ((seconds/totalTime) * 100000)).toInt()
        }

        override fun onFinish() {
            //Finish the quiz
            endQuiz()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPlayGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get difficulty from Home Activity
        gameDifficulty = intent.getStringExtra("difficulty").toString()

        // change scoring according to difficulty
        if(gameDifficulty == "medium") {
            bonusPerSecond = 2
        } else if(gameDifficulty == "expert") {
            bonusPerSecond = 3
        }

        // start a thread to put on a loading screen while fetching data
        val intent = Intent(this, SplashScreen::class.java)
        startActivity(intent)

        getQuestions()
        doBindings()
    }

    private fun doBindings() {
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

        // Create a an exit confirmation popup
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle("Exit game?")
            // Return the user to the home screen
            .setPositiveButton("Yes") { dialog, which ->
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                timer.cancel()
            }
            .setNegativeButton("No") { dialog, which ->
                exitDialog.cancel()
            }
        exitDialog = builder.create()

        binding.exitBtn.setOnClickListener {
            exitDialog.show()
        }
    }

    private fun getQuestions() {
        val questController = QuestionController()
        var qList: List<QuestionModel>
        val channel = Channel<List<QuestionModel>>()

        GlobalScope.launch {
            val data = questController.getAllByDifficulty(gameDifficulty)

            channel.send(data)
        }
        runBlocking { qList = channel.receive() }

        val randomNumbers = mutableSetOf<Int>()
        while (randomNumbers.size < 5) {
            randomNumbers.add(Random.nextInt(20))
        }

        for (num in randomNumbers) {
            qList[num].options = qList[num].options.shuffled()
            validAnswers.add(qList[num].correct)
            questionList.add(qList[num])
        }

        putQuestion()
        timer.start()
    }

    private fun putQuestion() {
        val currentQuestion = "Question ${questionIndex + 1}/5"

        binding.apply {
            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#949494")))
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#949494")))
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#949494")))
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#949494")))

            questionQuant.text = currentQuestion
            questionTitle.text = questionList[questionIndex].question
            option1.text = questionList[questionIndex].options[0]
            option2.text = questionList[questionIndex].options[1]
            option3.text = questionList[questionIndex].options[2]
            option4.text = questionList[questionIndex].options[3]
        }
    }

    private fun checkAnswer(answer: String, op: Int) {
        val index = questionList[questionIndex].options.indexOf(questionList[questionIndex].correct)
        if(answer == questionList[questionIndex].correct) {
            changeButtonColor(true, op, index)
            totalScore += scorePerQuestion
        } else {
            changeButtonColor(false, op, index)
        }
        answers.add(answer)

        // Delay 600ms for the player to see if they got the question right
        // then proceed to put another question or end the quiz
        Handler(Looper.getMainLooper()).postDelayed({
            questionIndex++
            buttonEnable(true)

            if(questionIndex < 5) {
                // put another question in
                putQuestion()
            } else {
                // end the quiz
                endQuiz()
            }
        }, 1500)
        buttonEnable(false)
    }

    private fun buttonEnable(state: Boolean) {
        if(state) {
            binding.apply {
                option1.isEnabled = true
                option2.isEnabled = true
                option3.isEnabled = true
                option4.isEnabled = true
            }
        } else {
            binding.apply {
                option1.isEnabled = false
                option2.isEnabled = false
                option3.isEnabled = false
                option4.isEnabled = false
            }
        }
    }

    // change button color according to the answer
    private fun changeButtonColor(state: Boolean, clicked: Int, index: Int) {
        if(state == true) {
            changeCorrectBg(index)
        } else {
            changeCorrectBg(index)
            if(clicked == 1) {
                binding.option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F34D37")))
            } else if(clicked == 2) {
                binding.option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F34D37")))
            } else if(clicked == 3) {
                binding.option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F34D37")))
            } else if(clicked == 4) {
                binding.option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F34D37")))
            }
        }
    }

    private fun changeCorrectBg(index: Int) {
        if(index == 0) {
            binding.option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94E161")))
        } else if(index == 1) {
            binding.option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94E161")))
        } else if(index == 2) {
            binding.option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94E161")))
        } else if(index == 3) {
            binding.option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94E161")))
        }
    }

    private fun endQuiz() {
        timer.cancel()

        // number of correct answers
        val correctAnswers: Int = totalScore / scorePerQuestion
        // time bonus: for each second remaining, adds 1 point to the bonus
        var timeBonus = 0
        var finalScore = 0

        if(correctAnswers != 0) {
            timeBonus = (questionTime - timeTaken) * bonusPerSecond
            finalScore = totalScore + timeBonus
        }

        val endGameVals = intArrayOf(correctAnswers, timeTaken, timeBonus, finalScore)

        val intent = Intent(this, EndGame::class.java)
        intent.putExtra("scores", endGameVals)
        intent.putExtra("difficulty", gameDifficulty)
        startActivity(intent)
        finish()
    }
}