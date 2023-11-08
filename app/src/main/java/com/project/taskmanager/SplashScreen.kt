package com.project.taskmanager
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {

    private val SplashTime : Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFE501")))
        supportActionBar?.setTitle("Local Favourite Finder")

        Handler().postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },SplashTime)
    }
}