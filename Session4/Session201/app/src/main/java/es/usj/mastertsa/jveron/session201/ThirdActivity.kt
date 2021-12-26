package es.usj.mastertsa.jveron.session201

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import es.usj.mastertsa.jveron.session201.databinding.ActivityThirdBinding
import org.jetbrains.anko.doAsync

class ThirdActivity : AppCompatActivity() {

    var url : String? = Uri.parse(Environment.getExternalStorageDirectory().path +
            "/Music/sound.mp3").path
    var position = 0
    var mediaPlayer: MediaPlayer? = null
    var permissionGranted = false

    private val bindings: ActivityThirdBinding by lazy {
        ActivityThirdBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)

        askForPermission()
        bindings.btnStart.setOnClickListener { doAsync { start() } }
        bindings.btnPause.setOnClickListener { doAsync { pause() } }
        bindings.btnResume.setOnClickListener { doAsync { resume() } }
        bindings.btnStop.setOnClickListener { doAsync { stop() } }
        bindings.tBtnRepeat.setOnClickListener { doAsync { repeat() } }
    }

    private fun askForPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    0
                )
            }
        } else {
            permissionGranted = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> permissionGranted = (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED)
            else -> permissionGranted = false
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
            MediaPlayer.create(this, Uri.parse(url))
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
        if (mediaPlayer != null && mediaPlayer!!.isPlaying()) {
            position = mediaPlayer!!.getCurrentPosition()
            mediaPlayer!!.pause()
        }
    }
    private fun resume() {
        if (mediaPlayer != null && !mediaPlayer!!.isPlaying()) {
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