package es.usj.mastertsa.jveron.session406

import android.app.ProgressDialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import es.usj.mastertsa.jveron.session406.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var button: Button? = null
    private var time: EditText? = null
    private var finalResult: TextView? = null

    private val bindings: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.btnRun.setOnClickListener {
            scope.launch {
                val response = try {
                    val time = Integer.parseInt(bindings.etTime.text.toString()) * 1000L
                    Thread.sleep(time)
                    "Slept for " + time/1000 + " seconds"
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    e.message.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                    e.message.toString()
                }
                withContext(Dispatchers.Main){

                    bindings.tvResult.text = response
                }
            }
        }
    }

    private inner class AsyncTaskRunner : AsyncTask<String, String, String>() {
        private lateinit var response: String
        private lateinit var progressDialog: ProgressDialog
        override fun doInBackground(vararg params: String): String?
        {
            publishProgress("Sleeping...") // Call onProgressUpdate();
            response = try {
                val time = Integer.parseInt(params[0]) * 1000L
                Thread.sleep(time)
                "Slept for " + params[0] + " seconds"
            } catch (e: InterruptedException) {
                e.printStackTrace()
                e.message.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                e.message.toString()
            }
            return response
        }
        override fun onPostExecute(result: String) {
            progressDialog.dismiss()
            finalResult!!.text = result
        }
        override fun onPreExecute() {
            progressDialog = ProgressDialog.show(
                this@MainActivity,
                "ProgressDialog",
                "Wait for " + time!!.text.toString() + " seconds"
            )
        }
        override fun onProgressUpdate(vararg text: String) {
            finalResult!!.text = text[0]
        }
    }
}