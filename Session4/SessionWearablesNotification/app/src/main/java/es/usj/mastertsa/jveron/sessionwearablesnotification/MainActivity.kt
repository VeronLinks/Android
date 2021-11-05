package es.usj.mastertsa.jveron.sessionwearablesnotification

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import es.usj.mastertsa.jveron.sessionwearablesnotification.databinding.ActivityMainBinding
import es.usj.mastertsa.jveron.shared.NotificationDatabase
import es.usj.mastertsa.jveron.shared.NotificationUtils

class MainActivity : AppCompatActivity() {

    private val bindings : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val notificationManager : NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        showNotification(true)
        bindings.btnNotify.setOnClickListener{
            showNotification(true)
        }
    }

    override fun onStop() {
        super.onStop()
        showNotification(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        showNotification(false)
    }

    private fun showNotification(isStarting: Boolean) {
        val enabled = notificationManager.areNotificationsEnabled()
        if (!enabled){
            val snackBar = Snackbar.make(
                findViewById(R.id.constraintLayout),
                "Enable notifications",
                Snackbar.LENGTH_LONG
            ).setAction("ENABLE"){
                openNotificationsSettingsForApp()
            }.show()
        }
        generateNotification(isStarting)
    }

    private fun generateNotification(starting: Boolean) {
        var notification = if (starting) NotificationDatabase.enteringNotification
            else NotificationDatabase.existingNotification(applicationContext)
        val channelId = NotificationUtils.createNotificationChannel(this, notification)
        val notificationStyle = NotificationCompat.BigTextStyle()
            .bigText(notification.text)
            .setBigContentTitle(notification.title)
            .setSummaryText(notification.summary)

        val notifyIntent = Intent(this, MainActivity::class.java)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val notifyPendingIntent = PendingIntent.getActivity(
            this,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationCompatBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext, channelId!!
        )
        GlobalNotificationBuilder.notificationCompatBuilderInstance = notificationCompatBuilder

        val playingNotification: Notification = notificationCompatBuilder
            .setStyle(notificationStyle)
            .setContentTitle(notification.contentTitle)
            .setContentText(notification.contentText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.mipmap.ic_launcher
                )
            )
            .setContentIntent(notifyPendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setColor(ContextCompat.getColor(applicationContext, R.color.purple_500))
            .setCategory(Notification.CATEGORY_REMINDER)
            .setPriority(notification.priority)
            .setVisibility(notification.channelLockscreenVisibility)
            .build()
        notificationManager.notify(
            NOTIFICATION_ID,
            playingNotification
        )
    }

    companion object {
        const val NOTIFICATION_ID = 888
    }

    private fun openNotificationsSettingsForApp() {
        val intent = Intent()
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("app_package", packageName)
        intent.putExtra("app_uid", applicationInfo.uid)
        startActivity(intent)
    }
}