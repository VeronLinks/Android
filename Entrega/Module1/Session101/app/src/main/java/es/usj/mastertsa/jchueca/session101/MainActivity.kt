package es.usj.mastertsa.jchueca.session101

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import es.usj.mastertsa.jchueca.session101.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val bindings: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.btnAdd.setOnClickListener { add() }
    }

    @SuppressLint("SetTextI18n")
    private fun add() {
        bindings.tvResult.text =
            "${bindings.etFirstValue.toInt() + bindings.etSecondValue.toInt()}"
    }

}

fun EditText.toInt(): Int {
    return if (text.isNullOrEmpty()) 0 else text.toString().toInt()
}