package es.usj.mastertsa.jveron.wearable

import android.app.Activity
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import es.usj.mastertsa.jveron.wearable.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val notificationManager : NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationManager
        setContentView(binding.root)
    }
}