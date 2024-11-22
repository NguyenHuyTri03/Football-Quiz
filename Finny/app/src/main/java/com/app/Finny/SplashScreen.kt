package com.app.Finny

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
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