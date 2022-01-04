package es.usj.mastertsa.jchueca.finalproject.notifications

import android.annotation.SuppressLint
import androidx.core.app.NotificationCompat

object GlobalNotificationBuilder {
    @SuppressLint("StaticFieldLeak")
    var notificationCompatBuilderInstance: NotificationCompat.Builder? = null
}