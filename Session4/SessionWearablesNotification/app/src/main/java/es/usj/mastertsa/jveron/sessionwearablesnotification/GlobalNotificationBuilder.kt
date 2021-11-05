package es.usj.mastertsa.jveron.sessionwearablesnotification

import android.annotation.SuppressLint
import androidx.core.app.NotificationCompat

object GlobalNotificationBuilder {
    @SuppressLint("StaticFieldLeak")
    var notificationCompatBuilderInstance: NotificationCompat.Builder? = null
}