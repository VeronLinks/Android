package es.usj.mastertsa.jveron.session501

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastertsa.jveron.session501.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    companion object{
        const val IP = "8.8.8.8"
    }

    private val bindings : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)

        bindings.btnPing.setOnClickListener {
            scope.launch {
                val process = Runtime.getRuntime().exec("ping -c 8 $IP")
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                val buffer = StringBuilder()
                reader.lines().forEach { buffer.append("$it\n") }
                withContext(Dispatchers.Main) {
                    bindings.tvResult.text = buffer.toString()
                }
            }
        }
    }
}