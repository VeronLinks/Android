package es.usj.mastertsa.jchueca.session101

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastertsa.jchueca.session101.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    
    private val bindings: ActivitySecondBinding by lazy {
        ActivitySecondBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.btnExecute.setOnClickListener { execute() }
    }

    private fun execute() {
        when(bindings.rbAddition.isChecked) {
            true -> bindings.tvResult.text =
                    "${bindings.etFirstValue.toInt() + bindings.etSecondValue.toInt()}"
            false -> bindings.tvResult.text =
                    "${bindings.etFirstValue.toInt() - bindings.etSecondValue.toInt()}"
        }

    }
}