package es.usj.mastertsa.jchueca.session101

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import es.usj.mastertsa.jchueca.session101.databinding.ActivitySeventhBinding
import kotlin.random.Random

class SeventhActivity : AppCompatActivity() {

    private val bindings: ActivitySeventhBinding by lazy {
        ActivitySeventhBinding.inflate(layoutInflater)
    }

    var randomValue : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        randomValue = Random.nextInt(0, 100000)
        Toast.makeText(this, "$randomValue", Toast.LENGTH_SHORT).show()
        bindings.btnCheck.setOnClickListener { check() }

    }

    private fun check() {
        var message = ""
        if(randomValue == bindings.etNumber.toInt())
            message = "Excelent, you remembered the number!"
        else
            message = "Sorry, you forgot the number!"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}