package es.usj.mastertsa.jveron.session305

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import es.usj.mastertsa.jveron.session305.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var permissionGranted = false
    private var uri: Uri? = null

    private val bindings : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.btnLoadVideo.setOnClickListener {
            loadVideos()
        }
        bindings.btnGetVideo.setOnClickListener {
            playVideo()
        }
        bindings.btnTakeVideo.setOnClickListener {
            cameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            permissionGranted = granted
            with(bindings.root) {
                when {
                    granted -> {
                        snackBar("Permission granted!")
                        recordVideo()
                    }

                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                    -> {
                        snackBar("Permission denied, show more info!")
                    }
                    else -> snackBar("Permission denied")
                }
            }
        }
    private val takeVideo =
        registerForActivityResult(ActivityResultContracts.TakeVideo()) {
            if (it != null && uri != null) {
                bindings.videoView.setVideoURI(uri)
                Toast.makeText(this, "Photo taken!",
                    Toast.LENGTH_SHORT).show()
            }
        }

    private fun playVideo() {
        bindings.videoView.setVideoURI(uri)
        bindings.videoView.start()
    }

    private fun recordVideo() {
        val filename = bindings.etFilename.text.toString()
        val videoUri =
            MediaStore.Video.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val videoDetails = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, filename)
        }
        contentResolver.insert(videoUri, videoDetails).let {
            uri = it
            takeVideo.launch(uri)
        }
    }

    private fun loadVideos() {
        val listIntent = Intent(this@MainActivity,
            MainBActivity::class.java)
        startActivity(listIntent)
    }
}

fun View.snackBar(message: String, duration: Int =
    BaseTransientBottomBar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}
