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
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Timer
import kotlin.random.Random


class PlayGame : AppCompatActivity() {
    private lateinit var binding: ActivityPlayGameBinding
    private val db = FirebaseFirestore.getInstance()
    private var questionList = mutableListOf<QuestionModel>()
    private lateinit var gameDifficulty: String
    private lateinit var questController: QuestionController
    private lateinit var exit_dialog: AlertDialog

    private val questionTime = 60
    private val scorePerQuestion = 10
    private var questionIndex = 0
    private var timeTaken = 0
    private var totalScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPlayGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get difficulty from Home Activity
        gameDifficulty = intent.getStringExtra("difficulty").toString()

        questController = QuestionController()

        // start a thread to put on a loading screen while fetching data
        Thread {
            startActivity(Intent(this, SplashScreen::class.java))

            questController.getAllByDifficulty(gameDifficulty) { qList ->
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

        // Create a an exit confirmation popup
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle("Exit game?")
            // Return the user to the home screen
            .setPositiveButton("Yes") { dialog, which ->
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, which ->
                exit_dialog.cancel()
            }
        exit_dialog = builder.create()

        binding.exitBtn.setOnClickListener {
            exit_dialog.show()
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
        println("Answer: ${answer}")
        if(answer == questionList[questionIndex].correct) {
            changeButtonColor(true, op)
            totalScore += scorePerQuestion
        } else {
            changeButtonColor(false, op)
        }

        // Delay 1s for the player to see if they got the question right
        // then proceed to put another question or end the quiz
        Handler(Looper.getMainLooper()).postDelayed({
            questionIndex++

            if(questionIndex < 5) {
                // put another question in
                putQuestion()
            } else {
                // end the quiz
                endQuiz()
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
                binding.option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94E161")))
            } else if(op == 4) {
                binding.option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#94E161")))
            }
        } else {
            if(op == 1) {
                binding.option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F34D37")))
            } else if(op == 2) {
                binding.option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F34D37")))
            } else if(op == 3) {
                binding.option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F34D37")))
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
                timeTaken++
                binding.progressBar.progress = (100 - ((seconds/totalTime) * 100000)).toInt()
            }

            override fun onFinish() {
                //Finish the quiz
                endQuiz()
            }
        }.start()
    }

    private fun endQuiz() {
        Timer().cancel()

        // number of correct answers
        val correctAnswers: Int = totalScore / scorePerQuestion
        // time bonus: for each second remaining, adds 1 point to the bonus
        var timeBonus = 0
        var finalScore = 0

        if(correctAnswers != 0) {
            timeBonus = questionTime - timeTaken
            finalScore = totalScore + timeBonus
        }

        val endGameVals = intArrayOf(correctAnswers, timeTaken, timeBonus, finalScore)

        val intent = Intent(this, EndGame::class.java)
        intent.putExtra("scores", endGameVals)
        intent.putExtra("difficulty", gameDifficulty)
        startActivity(intent)
        finish()
    }

//    fun getAllByDifficulty(difficulty: String, callback: (res: List<QuestionModel>) -> Unit) {
//        var questions = mutableListOf<QuestionModel>()
//
//        db.collection("${difficulty}_questions").get()
//            .addOnSuccessListener { documents ->
//                var i = 0
//                for(document in documents) {
//                    val data = document.data
//                    val option_list: List<String> = data.get("options") as List<String>
//
//                    val question = QuestionModel(document.id, data.get("image_url").toString(), data.get("question").toString(), option_list, data.get("correct").toString())
//                    questions.add(question)
//                }
//
//                callback.invoke(questions)
//            }
//    }
}