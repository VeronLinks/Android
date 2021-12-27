package es.usj.mastertsa.jveron.session405

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import java.util.*

class FifthSplashActivity : AppCompatActivity() {

    companion object {
        private val SPLASH_SCREEN_DELAY: Long = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_fifth_splash)
        val task = object : TimerTask() {
            override fun run() {
                val intent = Intent(this@FifthSplashActivity,
                    MainActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }
        val timer = Timer()
        timer.schedule(task, SPLASH_SCREEN_DELAY)
    }
}