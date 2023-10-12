package com.example.bcs421_homework01

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private val timeout: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //displays splash screen and moves to the question activity after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, QuestionActivity::class.java)
            startActivity(intent)
            finish()
        }, timeout)
    }
}