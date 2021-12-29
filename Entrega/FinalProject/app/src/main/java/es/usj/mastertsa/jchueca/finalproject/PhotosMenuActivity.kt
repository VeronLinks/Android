package es.usj.mastertsa.jchueca.finalproject

import android.R.layout
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import es.usj.mastertsa.jchueca.finalproject.databinding.ActivityPhotosMenuBinding

class PhotosMenuActivity : AppCompatActivity() {

    private val files : MutableList<Uri> = mutableListOf()
    private val titles : MutableList<String> = mutableListOf()
    private lateinit var bindings : ActivityPhotosMenuBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityPhotosMenuBinding.inflate(layoutInflater)
        setContentView(bindings.root)
        supportActionBar!!.hide()

        retrievePhotoList()
        bindings.lvPhotos.adapter = ArrayAdapter(this,
            layout.simple_list_item_1, titles)
        bindings.lvPhotos.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                bindings.ivSelectedPhoto.setImageURI(files[position])
            }
        bindings.btnBack.setOnClickListener {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun retrievePhotoList() {
        val externalUri: Uri =
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.TITLE,
            MediaStore.MediaColumns.RELATIVE_PATH
        )
        val cursor = contentResolver.query(
            externalUri, projection, null, null,
            MediaStore.Images.Media.DATE_TAKEN + " DESC"
        )
        val idColumn: Int? =
            cursor?.getColumnIndex(MediaStore.MediaColumns._ID)
        val titleColumn: Int? =
            cursor?.getColumnIndex(MediaStore.MediaColumns.TITLE)

        while (cursor?.moveToNext() == true) {

            var currentTitle = cursor.getString(titleColumn!!)
            if (currentTitle.startsWith(PHOTOS_TITLE_PREFIX)) {

                currentTitle = currentTitle.removePrefix(PHOTOS_TITLE_PREFIX)
                if (currentTitle.length > PHOTOS_TITLE_FORMAT.length) {
                    currentTitle = currentTitle.dropLast(currentTitle.length - PHOTOS_TITLE_FORMAT.length)
                }
                currentTitle = "Reward from " + currentTitle.replace('_', '/')
                titles.add(currentTitle)

                val photoUri: Uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    idColumn?.let { cursor.getString(it) }
                )
                files.add(photoUri)
            }
        }

        cursor?.close()
    }

}