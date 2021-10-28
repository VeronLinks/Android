package es.usj.mastertsa.jveron.session203

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import es.usj.mastertsa.jveron.session203.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val bindings : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val scope = CoroutineScope(Dispatchers.Default)

    private val url : Uri? by lazy {

        var audioUri : Uri? = null

        val externalUri: Uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
        //val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val projection = arrayOf(
            MediaStore.DownloadColumns._ID,
            MediaStore.DownloadColumns.TITLE,
            MediaStore.DownloadColumns.RELATIVE_PATH
        )
        val cursor = contentResolver.query(
            externalUri, projection, null, null,
            MediaStore.DownloadColumns.DATE_ADDED + " DESC"
        )
        val relativePath: Int? = cursor?.getColumnIndex(MediaStore.DownloadColumns._ID)
        while (cursor?.moveToNext() == true) {

            audioUri = Uri.withAppendedPath(
                externalUri,
                relativePath?.let {
                    cursor.getString(it)
                }
            )

        }
        cursor?.close()
        audioUri
    }
    var position = 0
    var mediaPlayer: MediaPlayer? = null
    var permissionGranted = false

    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        permissionGranted = isGranted
        if (isGranted) {
            start()
        }
        else {
            Toast.makeText(this, "Sound requires permissions", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.btnStart.setOnClickListener {
            scope.launch{
                startWithPermission()
                //withContext(Dispatchers.Main) {
                //    bindings.btnStart.text = ""
                //}
            }
        }
        bindings.btnPause.setOnClickListener { scope.launch{ pause() } }
        bindings.btnResume.setOnClickListener { scope.launch{ resume() } }
        bindings.btnStop.setOnClickListener { scope.launch{ stop() } }
        bindings.tBtnRepeat.setOnClickListener { scope.launch{ repeat() } }
    }

    private fun startWithPermission(){
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                permissionGranted = true
                start()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                Toast.makeText(
                    this,
                    "Please, please, playing sound requires permissions!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                permissionRequest.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun createMediaPlayer(): MediaPlayer {
        if(!permissionGranted)
            runOnUiThread{
                bindings.swSource.isChecked = true
            }
        return if (bindings.swSource.isChecked) {
            MediaPlayer.create(this, R.raw.sound)
        } else {
            MediaPlayer.create(this, Uri.parse(url.toString()))
        }
    }

    private fun destroy() {
        if (mediaPlayer != null)
            mediaPlayer!!.release()
    }

    private fun start() {
        destroy()
        mediaPlayer = createMediaPlayer()
        mediaPlayer!!.isLooping = bindings.tBtnRepeat.isChecked
        mediaPlayer!!.start()
    }

    private fun pause() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            position = mediaPlayer!!.currentPosition
            mediaPlayer!!.pause()
        }
    }

    private fun resume() {
        if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
            mediaPlayer!!.seekTo(position)
            mediaPlayer!!.start()
        }
    }

    private fun stop() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            position = 0
        }
    }

    private fun repeat() {
        stop()
        start()
    }

}