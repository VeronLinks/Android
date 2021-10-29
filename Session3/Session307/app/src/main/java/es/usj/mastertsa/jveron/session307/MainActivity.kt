package es.usj.mastertsa.jveron.session307

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.view.Gravity
import android.widget.Toast
import com.google.gson.Gson
import es.usj.mastertsa.jveron.session307.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    companion object {
        private const val URL =
            "https://www.dropbox.com/s/2psyzixpaq26s7k/countries.json?dl=1"
    }

    private var downloadManager: DownloadManager? = null
    private var downloadReference: Long = 0

    private val bindings: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val referenceId =
                intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadReference == referenceId) {
                bindings.btnCancel.isEnabled = false
                var ch: Int
                val file: ParcelFileDescriptor
                val strContent = StringBuffer("")
                val countries = StringBuffer("")
                try {
                    file =
                        downloadManager!!.openDownloadedFile(downloadReference)
                    val fileInputStream =
                        ParcelFileDescriptor.AutoCloseInputStream(file)
                    ch = fileInputStream.read()
                    while (ch != -1) {
                        strContent.append(ch.toChar())
                        ch = fileInputStream.read()
                    }
                    val responseObj =
                        JSONObject(strContent.toString())
                    val countriesObj =
                        responseObj.getJSONArray("countries")
                    for (i in 0 until countriesObj.length()) {
                        val countryInfo =
                            countriesObj.getJSONObject(i).toString()
                        val country =
                            Gson().fromJson<Country>(countryInfo, Country::class.java)
                        countries.append(country.name + ": " +
                                country.code + "\n")
                    }
                    bindings.countryData.text = countries.toString()
                    val toast = Toast.makeText(
                        this@MainActivity,
                        "Downloading of data just finished",
                        Toast.LENGTH_LONG
                    )
                    toast.setGravity(Gravity.TOP, 25, 400)
                    toast.show()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.btnStart.setOnClickListener { download() }
        bindings.btnCancel.setOnClickListener { cancel() }
        bindings.btnCheck.setOnClickListener { check() }
        bindings.btnDisplay.setOnClickListener { display() }

        bindings.btnCheck.isEnabled = false
        bindings.btnCancel.isEnabled = false
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(downloadReceiver, filter)
    }

    private fun download() {
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as
                DownloadManager
        val uri = Uri.parse(URL)
        val request = DownloadManager.Request(uri)

        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
        )
        request.setAllowedOverRoaming(false)
        request.setTitle("My Data Download")
        request.setDescription("Android Data download using DownloadManager.")
        request.setDestinationInExternalFilesDir(
            this,
            Environment.DIRECTORY_DOWNLOADS,
            "countries.json"
        )
        downloadReference = downloadManager!!.enqueue(request)
        bindings.countryData.text = "Getting data from Server,Please WAIT..."
        bindings.btnCheck.isEnabled = true
        bindings.btnCancel.isEnabled = true
    }

    private fun display() {
        val intent = Intent()
        intent.action = DownloadManager.ACTION_VIEW_DOWNLOADS
        startActivity(intent)
    }

    private fun check() {
        val myDownloadQuery = DownloadManager.Query()
        myDownloadQuery.setFilterById(downloadReference)
        val cursor = downloadManager!!.query(myDownloadQuery)
        if (cursor.moveToFirst()) {
            checkStatus(cursor)
        }
    }

    private fun cancel() {
        downloadManager!!.remove(downloadReference)
        bindings.btnCheck.isEnabled = false
        bindings.countryData.text = "Download of the file cancelled..."
    }

    private fun checkStatus(cursor: Cursor) {
        val columnIndex =
            cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        val status = cursor.getInt(columnIndex)
        val columnReason =
            cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
        val reason = cursor.getInt(columnReason)
        val filenameIndex =
            cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
        val filename = cursor.getString(filenameIndex)
        var statusText = ""
        var reasonText = ""
        when (status) {
            DownloadManager.STATUS_FAILED -> {
                statusText = "STATUS_FAILED"
                when (reason) {
                    DownloadManager.ERROR_CANNOT_RESUME ->
                        reasonText = "ERROR_CANNOT_RESUME"
                    DownloadManager.ERROR_DEVICE_NOT_FOUND ->
                        reasonText = "ERROR_DEVICE_NOT_FOUND"
                    DownloadManager.ERROR_FILE_ALREADY_EXISTS ->
                        reasonText = "ERROR_FILE_ALREADY_EXISTS"
                    DownloadManager.ERROR_FILE_ERROR -> reasonText =
                        "ERROR_FILE_ERROR"
                    DownloadManager.ERROR_HTTP_DATA_ERROR ->
                        reasonText = "ERROR_HTTP_DATA_ERROR"
                    DownloadManager.ERROR_INSUFFICIENT_SPACE ->
                        reasonText = "ERROR_INSUFFICIENT_SPACE"
                    DownloadManager.ERROR_TOO_MANY_REDIRECTS ->
                        reasonText = "ERROR_TOO_MANY_REDIRECTS"
                    DownloadManager.ERROR_UNHANDLED_HTTP_CODE ->
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE"
                    DownloadManager.ERROR_UNKNOWN -> reasonText =
                        "ERROR_UNKNOWN"
                }
            }
            DownloadManager.STATUS_PAUSED -> {
                statusText = "STATUS_PAUSED"
                when (reason) {
                    DownloadManager.PAUSED_QUEUED_FOR_WIFI ->
                        reasonText = "PAUSED_QUEUED_FOR_WIFI"
                    DownloadManager.PAUSED_UNKNOWN -> reasonText =
                        "PAUSED_UNKNOWN"
                    DownloadManager.PAUSED_WAITING_FOR_NETWORK ->
                        reasonText = "PAUSED_WAITING_FOR_NETWORK"
                    DownloadManager.PAUSED_WAITING_TO_RETRY ->
                        reasonText = "PAUSED_WAITING_TO_RETRY"
                }
            }
            DownloadManager.STATUS_PENDING -> statusText =
                "STATUS_PENDING"
            DownloadManager.STATUS_RUNNING -> statusText =
                "STATUS_RUNNING"
            DownloadManager.STATUS_SUCCESSFUL -> {
                statusText = "STATUS_SUCCESSFUL"
                reasonText = "Filename:\n$filename"
            }
        }
        val toast = Toast.makeText(
            this@MainActivity,
            statusText + "\n" + reasonText, Toast.LENGTH_LONG )
        bindings.countryData.text = statusText + reasonText
        toast.setGravity(Gravity.TOP, 25, 400)
        toast.show()
    }
}