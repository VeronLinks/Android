package com.example.session201fragments

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.session201fragments.databinding.ActivityFourthBinding
import java.io.File
import java.io.IOException

class FourthActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener {

    lateinit var recorder : MediaRecorder
    lateinit var player : MediaPlayer
    lateinit var file : File

    val RESPONSE = 0

    private val binding: ActivityFourthBinding by lazy {
        ActivityFourthBinding.inflate(layoutInflater)
    }

    private fun configureButtons(start: Boolean = true, pause: Boolean = true, play: Boolean = true) {
        binding.btnStartRecording.isEnabled = start
        binding.btnStopRecording.isEnabled = pause
        binding.btnPlayRecording.isEnabled = play
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnPlayRecording.setOnClickListener { play() }
        binding.btnStopRecording.setOnClickListener { stop() }
        binding.btnStartRecording.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), RESPONSE)
            } else {
                record()
            }
        }
        configureButtons(start = true, pause = false, play = false)

    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun record() {
        recorder = MediaRecorder()
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        val path = File(this.externalMediaDirs[0].path)
        try {
            file = File.createTempFile("temporal", ".3gp", path);
        } catch (e : IOException) {
        }
        recorder.setOutputFile(file.absolutePath);
        try {
            recorder.prepare();
        } catch (e : IOException) {
        }
        recorder.start();
        binding.tvMessage.text = getString(R.string.recording)
        configureButtons(start = false, pause = true, play = false)
    }

    private fun play() {
        player.start()
        configureButtons(start = false, pause = false, play = false)
        binding.tvMessage.text = getString(R.string.playing)
    }

    private fun stop() {
        recorder.stop()
        recorder.release()
        player = MediaPlayer()
        player.setOnCompletionListener(this)
        try {
            player.setDataSource(file.absolutePath)
        } catch (e: IOException) { }
        try {
            player.prepare()
        } catch (e: IOException) { }
        configureButtons(start = true, pause = false, play = true)
        binding.tvMessage.text = getString(R.string.ready)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        configureButtons()
        binding.tvMessage.text = getString(R.string.ready)
    }
}