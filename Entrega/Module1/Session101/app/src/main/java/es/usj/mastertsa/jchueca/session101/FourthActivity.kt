package es.usj.mastertsa.jchueca.session101

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import es.usj.mastertsa.jchueca.session101.databinding.ActivityFourthBinding

class FourthActivity : AppCompatActivity() {

    enum class Operation {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE
    }

    private val bindings: ActivityFourthBinding by lazy {
        ActivityFourthBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.spOperations.adapter = ArrayAdapter<String>(this,
                                            R.layout.simple_spinner_dropdown_item,
                                            Operation.values().map { it -> it.name })
        bindings.btnExecute.setOnClickListener { execute() }
    }

    private fun execute() {

        val firstValue = bindings.etFirstValue.toDouble()
        val secondValue = bindings.etSecondValue.toDouble()
        val operation = Operation.valueOf(bindings.spOperations.selectedItem.toString())
        var result = 0.0
        when(operation) {
            Operation.ADD -> result = firstValue + secondValue
            Operation.SUBTRACT -> result = firstValue - secondValue
            Operation.MULTIPLY -> result = firstValue * secondValue
            Operation.DIVIDE -> result = firstValue / secondValue
        }
        bindings.tvResult.text = "The result is: ${result}"
    }

    private fun EditText.toDouble(): Double =
        if(text.isNullOrEmpty()) 0.0 else text.toString().toDouble()
}

