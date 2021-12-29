package es.usj.mastertsa.jchueca.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    companion object {
        private val SPLASH_SCREEN_DELAY: Long = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_splash_screen)
        val task = object : TimerTask() {
            override fun run() {
                val intent = Intent(this@SplashScreenActivity,
                    ClaimRewardActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }
        val timer = Timer()
        timer.schedule(task, SPLASH_SCREEN_DELAY)
    }
}