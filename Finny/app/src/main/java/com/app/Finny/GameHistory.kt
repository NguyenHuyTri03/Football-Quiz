package com.app.Finny

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.Finny.databinding.ActivityGameHistoryBinding

class GameHistory : AppCompatActivity() {
    private lateinit var binding: ActivityGameHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGameHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}