package es.usj.mastertsa.jchueca.finalproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.core.content.ContextCompat
import es.usj.mastertsa.jchueca.finalproject.databinding.ActivityClaimRewardBinding
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

}