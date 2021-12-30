package es.usj.mastertsa.jchueca.finalproject.notifications

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import es.usj.mastertsa.jchueca.finalproject.R

object NotificationDatabase {
    const val TITLE = "title"
    const val SUMMARY = "summary"
    const val TEXT = "text"

    val enteringNotification : USJCampusNotification get() = USJCampusNotification.getInstance(
        Bundle()
    )

    fun existingNotification(context: Context) : USJCampusNotification {
        val bundle = Bundle()
        bundle.putString(TITLE, context.getString(R.string.notification_default_title))
        bundle.putString(SUMMARY, context.getString(R.string.notification_default_summary))
        bundle.putString(TEXT, context.getString(R.string.notification_default_text))
        return USJCampusNotification.getInstance(bundle)
    }

    class USJCampusNotification private constructor(): NotificationData() {

        public lateinit var text : String
        public lateinit var title : String
        public lateinit var summary : String

        companion object {
            private var INSTANCE : USJCampusNotification? = null

            @Synchronized
            fun getInstance(bundle: Bundle) : USJCampusNotification{
                if (INSTANCE == null) {
                    INSTANCE = USJCampusNotification()
                }
                INSTANCE!!.install(bundle)
                return INSTANCE!!
            }
        }

        @Synchronized
        private fun install(bundle: Bundle) {
            title = bundle.getString(TITLE, "Come back to Exercise is Fun to claim your rewards!")
            summary = bundle.getString(SUMMARY, "Challenges completed!")
            text = bundle.getString(TEXT, "Some challenges running in Exercise is Fun are now completed.")
            contentTitle = title
            contentText = summary
            channelName = contentTitle
        }

        override fun toString(): String {
            return "$title: $text"
        }

        init {
            priority = NotificationCompat.PRIORITY_DEFAULT
            channelId = "channel_reminder_1"
            channelDescription = "USJ Experience Notifications"
            channelImportance = NotificationManager.IMPORTANCE_DEFAULT
            isChannelEnableVibrate = false
            channelLockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        }
    }

    abstract class NotificationData {
        var contentTitle: String? = null
            protected set
        var contentText: String? = null
            protected set
        var priority = 0
            protected set

        // Channel values (O and above) get methods:
        // Notification channel values (O and above):
        var channelId: String? = null
            protected set
        var channelName: CharSequence? = null
            protected set
        var channelDescription: String? = null
            protected set
        var channelImportance = 0
            protected set
        var isChannelEnableVibrate = false
            protected set
        var channelLockscreenVisibility = 0
            protected set
    }
}