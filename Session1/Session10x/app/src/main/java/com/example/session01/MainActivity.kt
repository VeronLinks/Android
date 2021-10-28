package com.example.session01

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.session01.databinding.ActivityMainBinding

const val LOG_TAG = "LIFECYCLE"

class MainActivity : AppCompatActivity() {

    private var uri : Uri? = null;

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it != false) {
            bindings.imageView.setImageURI(uri)
        }
    }

    private val bindings : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Log.e(LOG_TAG, "onCreate")

        setContentView(bindings.root)
        bindings.tvHelloWorld.text = "Testing session 1"
        bindings.btnSwitchView.setOnClickListener{
            //bindings.tvHelloWorld.switchVisibility()
            cameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun takePhoto() {
        val filename = "photo.png"
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

    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    takePhoto()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {

                }
                else -> Log.v("Denied", "Permission denied")
            }
        }

    //override fun onStart() {
    //    super.onStart()
    //    Log.e(LOG_TAG, "onStart")
    //}
//
    //override fun onStop() {
    //    super.onStop()
    //    Log.e(LOG_TAG, "onStop")
    //}
//
    //override fun onDestroy() {
    //    super.onDestroy()
    //    Log.e(LOG_TAG, "onDestroy")
    //}
//
    //override fun onRestart() {
    //    super.onRestart()
    //    Log.e(LOG_TAG, "onRestart")
    //}
//
    //override fun onResume() {
    //    super.onResume()
    //    Log.e(LOG_TAG, "onResume")
    //}
//
    //override fun onPause() {
    //    super.onPause()
    //    Log.e(LOG_TAG, "onPause")
    //}
}

private fun TextView.switchVisibility() {
    visibility =
        if (visibility == View.VISIBLE)
            View.INVISIBLE
        else
            View.VISIBLE
}
