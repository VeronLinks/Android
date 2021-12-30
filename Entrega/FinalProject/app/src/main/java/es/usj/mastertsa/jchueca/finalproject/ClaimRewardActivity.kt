package es.usj.mastertsa.jchueca.finalproject

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import es.usj.mastertsa.jchueca.finalproject.databinding.ActivityClaimRewardBinding
import es.usj.mastertsa.jchueca.finalproject.notifications.GlobalNotificationBuilder
import es.usj.mastertsa.jchueca.finalproject.notifications.NotificationDatabase
import es.usj.mastertsa.jchueca.finalproject.notifications.NotificationUtils
import java.io.IOException
import java.util.*

const val REWARD = 100
const val PHOTOS_TITLE_PREFIX = "EIF_"
const val PHOTOS_TITLE_FORMAT = "dd_MM_yyyy"
const val PHOTOS_TITLE_SUFFIX = ".jpg"

class ClaimRewardActivity : AppCompatActivity() {

    private lateinit var bindings: ActivityClaimRewardBinding
    private var permissionGranted = false
    private var uri : Uri? = null
    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture())
        {
            if (it != false) {
                //bindings.imageView.setImageURI(uri)
                Toast.makeText(this, "Photo taken!",
                    Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityClaimRewardBinding.inflate(layoutInflater)
        setContentView(bindings.root)
        supportActionBar!!.hide()

        enableNotifications()
        askForPermission()

        bindings.btnBack.setOnClickListener {
            finish()
        }
        bindings.btnClaim.setOnClickListener {
            claimReward()
        }
        bindings.btnTakePhoto.setOnClickListener {
            takePhoto()
        }
    }

    override fun onResume() {
        super.onResume()
        getPicture()
    }

    @SuppressLint("SimpleDateFormat")
    private fun takePhoto() {
        val sdf = SimpleDateFormat(PHOTOS_TITLE_FORMAT)
        val filename = PHOTOS_TITLE_PREFIX + sdf.format(Date()) + PHOTOS_TITLE_SUFFIX
        System.out.println(filename)
        val imageUri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
        val imageDetails = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, filename)
        }
        contentResolver.insert(imageUri, imageDetails).let {
            uri = it
            takePicture.launch(uri)
        }
    }

    private fun claimReward() {

        SaveLoad.save(this, SaveLoad.load(this) + REWARD)

        val viewIntent = Intent(this, MainActivity::class.java)
        startActivity(viewIntent)
        finish()

        showNotification(true)

    }

    private fun getPicture() {
        if(uri != null) {
            var image: Bitmap? = null
            try {
                val source: ImageDecoder.Source =ImageDecoder.createSource(this.contentResolver, uri!!)
                image = ImageDecoder.decodeBitmap(source)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            bindings.imageView.setImageBitmap(image)
        }
    }

    private fun askForPermission() {
        val permissions = Manifest.permission.WRITE_EXTERNAL_STORAGE + Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(this, permissions)
            != PackageManager.PERMISSION_GRANTED) {
            if
                    (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permissions)) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(permissions), 0)
            }
        } else {
            permissionGranted = true
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions,
            grantResults)
        permissionGranted = when (requestCode) {
            0 -> (grantResults.isNotEmpty() &&
                    grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED)
            else -> false
        }
    }

    // NOTIFICATIONS

    private val notificationManager : NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(applicationContext)
    }

    private fun enableNotifications() {
        val enabled = notificationManager.areNotificationsEnabled()
        if (!enabled){
            val snackBar = Snackbar.make(
                findViewById(R.id.mainLayout), // TODO: Desde recompensas
                "Enable notifications",
                Snackbar.LENGTH_LONG
            ).setAction("ENABLE"){
                openNotificationsSettingsForApp()
            }.show()
        }
    }

    private fun showNotification(isStarting: Boolean) {
        enableNotifications()
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

        val notifyIntent = Intent(this, MainActivity::class.java) // TODO: Redirigir a recompensas
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
            current_id,
            playingNotification
        )
    }

    companion object {
        var current_id = 0
    }

    private fun openNotificationsSettingsForApp() {
        val intent = Intent()
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("app_package", packageName)
        intent.putExtra("app_uid", applicationInfo.uid)
        startActivity(intent)
    }

}