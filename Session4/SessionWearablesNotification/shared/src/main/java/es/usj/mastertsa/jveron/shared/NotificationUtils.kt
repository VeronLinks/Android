package es.usj.mastertsa.jveron.shared

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationUtils {

    private val levels = arrayOf(
        NotificationManager.IMPORTANCE_UNSPECIFIED,
        NotificationManager.IMPORTANCE_NONE,
        NotificationManager.IMPORTANCE_MIN,
        NotificationManager.IMPORTANCE_LOW,
        NotificationManager.IMPORTANCE_DEFAULT,
        NotificationManager.IMPORTANCE_HIGH)

    fun createNotificationChannel(
        context: Context,
        notificationData: NotificationDatabase.NotificationData
    ): String? {
        // The id of the channel.
        val channelId = notificationData.channelId

        // The user-visible name of the channel.
        val channelName = notificationData.channelName

        // The user-visible description of the channel.
        val channelDescription = notificationData.channelDescription
        val channelImportance = notificationData.channelImportance
        val channelEnableVibrate = notificationData.isChannelEnableVibrate
        val channelLockscreenVisibility = notificationData.channelLockscreenVisibility

        // Initializes NotificationChannel.
        val notificationChannel = NotificationChannel(channelId, channelName,
            levels.firstOrNull { it == channelImportance }
                ?: NotificationManager.IMPORTANCE_UNSPECIFIED)

        notificationChannel.description = channelDescription
        notificationChannel.enableVibration(channelEnableVibrate)
        notificationChannel.lockscreenVisibility = channelLockscreenVisibility

        // Adds NotificationChannel to system. Attempting to create an existing notification
        // channel with its original values performs no operation, so it's safe to perform the
        // below sequence. http://www.tech-recipes.com/rx/49586/how-do-i-connect-an-android-wear-emulator-to-a-real-phone/
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        return channelId
    }
}
