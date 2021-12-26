package es.usj.mastertsa.jveron.session201

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import es.usj.mastertsa.jveron.session201.databinding.ActivityFourthBinding
import java.io.File
import java.io.IOException

class FourthActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener {

    lateinit var recorder: MediaRecorder
    lateinit var player: MediaPlayer
    lateinit var file: File

    val RESPONSE = 0

    private val bindings: ActivityFourthBinding by lazy {
        ActivityFourthBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)

        bindings.btnPlayRecording.setOnClickListener { play() }
        bindings.btnStopRecording.setOnClickListener { stop() }
        bindings.btnStartRecording.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), RESPONSE
                )
            } else {
                record()
            }
        }
        configureButtons(start = true, pause = false, play = false)
    }

    private fun configureButtons(
        start: Boolean = true,
        pause: Boolean = true,
        play: Boolean = true
    ) {
        bindings.btnStartRecording.isEnabled = start
        bindings.btnStopRecording.isEnabled = pause
        bindings.btnPlayRecording.isEnabled = play
    }

    private fun stop() {
        recorder.stop()
        recorder.release()
        player = MediaPlayer()
        player.setOnCompletionListener(this)
        try {
            player.setDataSource(file.absolutePath)
        } catch (e: IOException) {
        }
        try {
            player.prepare()
        } catch (e: IOException) {
        }
        configureButtons(start = true, pause = false, play = true)
        bindings.tvMessage.text = getString(R.string.ready)
    }

    private fun play() {
        player.start()
        configureButtons(start = false, pause = false, play = false)
        bindings.tvMessage.text = getString(R.string.playing)
    }

    private fun record() {

        if (!Environment.isExternalStorageManager()) {
            intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivity(intent);
            return;
        }

        recorder = MediaRecorder()
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        val path = File(Environment.getExternalStorageDirectory().path)
        try {
            file = File.createTempFile("temporal", ".3gp", path)
        } catch (e : IOException) {
            e.printStackTrace()
        }
        recorder.setOutputFile(file.absolutePath);
        try {
            recorder.prepare();
        } catch (e : IOException) {
        }
        recorder.start();
        bindings.tvMessage.text = getString(R.string.recording)
        configureButtons(start = false, pause = true, play = false)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        configureButtons()
        bindings.tvMessage.text = getString(R.string.ready)
    }

}