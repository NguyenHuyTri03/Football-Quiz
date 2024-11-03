package com.app.Finny

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.Finny.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var biding: ActivitySplashScreenBinding
    private var loadTime: Long = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        biding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(biding.root)

        Handler(Looper.myLooper()!!).postDelayed({
            finish()
        }, loadTime)
    }
}