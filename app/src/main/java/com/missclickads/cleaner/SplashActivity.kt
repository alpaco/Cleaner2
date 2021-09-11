package com.missclickads.cleaner

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarSplash)
        val animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        animation.duration = (8.5 * 1000).toLong()
        animation.start()
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
        }, (8.5 * 1000).toLong())
    }



}