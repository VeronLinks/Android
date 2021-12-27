package es.usj.mastertsa.jveron.session401.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import android.widget.Toast

class DelayedMessageService : JobService() {

    private fun showText(text: String?) {
        Log.v(LOG_TAG, "The message is: $text")
        Toast.makeText(this, "The message is: $text", Toast.LENGTH_LONG).show()
    }
    companion object {
        const val LOG_TAG = "DMS_LOG"
        const val EXTRA_MESSAGE = "Important notice"
    }
    override fun onStartJob(params: JobParameters?): Boolean {
        synchronized(this) {
            Thread.sleep(1000)
            var text = params?.extras?.getString(EXTRA_MESSAGE) ?: "No message"
            showText(text)
        }
        return true
    }
    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

}