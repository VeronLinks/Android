package es.usj.mastertsa.jveron.session402

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import es.usj.mastertsa.jveron.session402.services.BoundService

class MainActivity : AppCompatActivity() {

    private lateinit var tvTimeStamp: TextView
    private var isBound = false
    lateinit var service: BoundService

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
        override fun onServiceConnected(name: ComponentName?, serviceBinder: IBinder?) {
            val boundServiceBinder = serviceBinder as BoundService.BoundServiceBinder
            service = boundServiceBinder.service
            isBound = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnStop = findViewById<Button>(R.id.btnStop)
        val btnPrintTimestamp = findViewById<Button>(R.id.btnPrintTimestamp)
        tvTimeStamp = findViewById(R.id.tvTimeStamp)

        btnStop.setOnClickListener { stop() }
        btnPrintTimestamp.setOnClickListener { printTimeStamp() }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, BoundService::class.java)
        startService(intent)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }
    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }


    private fun printTimeStamp() {
        if (isBound) {
            tvTimeStamp.text = service.getTimestamp()
        }
    }

    private fun stop() {
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
        val intent = Intent(
            this@MainActivity,
            BoundService::class.java
        )
        stopService(intent)
    }
}