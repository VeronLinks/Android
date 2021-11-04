package es.usj.mastertsa.jveron.session401

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import es.usj.mastertsa.jveron.session401.databinding.ActivityMainBinding
import es.usj.mastertsa.jveron.session401.services.DelayedMessageService

class MainActivity : AppCompatActivity() {

    private val bindings : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.btnSendMessage.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val text = bindings.etMessage.text.toString()
        val serviceComponent = ComponentName(this, DelayedMessageService::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency(3000L)
        builder.setOverrideDeadline(6000L)
        builder.setExtras(
            PersistableBundle().apply {
                putString(DelayedMessageService.EXTRA_MESSAGE, text)
            }
        )
        val service = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        service.schedule(builder.build())
    }
}