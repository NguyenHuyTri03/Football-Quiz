package com.app.Finny.Activities

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Adapters.TutorialAdapter
import com.app.Finny.R
import com.app.Finny.SoundManager
import com.app.Finny.databinding.ActivityTutorialBinding

class Tutorial : AppCompatActivity() {
    private lateinit var binding: ActivityTutorialBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var imgCount = 0
        val imgList = mutableListOf<Int>()
        val descriptionList = mutableListOf<String>()
        imgList.add(R.drawable.start)
        descriptionList.add("Choose Start game to start playing")

        imgList.add(R.drawable.difficulty)
        descriptionList.add("Choose a difficulty")

        imgList.add(R.drawable.play)
        descriptionList.add("Have fun!!")

        val text = "${imgCount + 1}/3"
        binding.imgCount.text = text
        binding.tutorialImg.setImageResource(imgList[imgCount])
        binding.description.text = descriptionList[imgCount]

        binding.nextBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")

            if(imgCount < 2) {
                imgCount++

                binding.tutorialImg.setImageResource(imgList[imgCount])
                binding.description.text = descriptionList[imgCount]
                val text = "${imgCount + 1}/3"
                binding.imgCount.text = text
            }
        }
        binding.prevBtn.setOnClickListener {
            SoundManager.playSFX(this, "answer_click")

            if(imgCount > 0) {
                imgCount--

                binding.tutorialImg.setImageResource(imgList[imgCount])
                binding.description.text = descriptionList[imgCount]
                val text = "${imgCount + 1}/3"
                binding.imgCount.text = text
            }
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        SoundManager.mediaPlayer?.pause() // Pause music when app is in the background
    }

    override fun onResume() {
        super.onResume()
        SoundManager.mediaPlayer?.start() // Resume music when app comes back to the foreground
    }
}