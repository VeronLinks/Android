package es.usj.mastertsa.jchueca.session101

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastertsa.jchueca.session101.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {
    private val bindings: ActivityThirdBinding by lazy {
        ActivityThirdBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.btnExecute.setOnClickListener { execute() }
    }

    private fun execute() {
        var result = ""
        if(bindings.cbPlus.isChecked)
            result += "The addition is " +
                    "${ bindings.etFirstValue.toInt() + bindings.etSecondValue.toInt()}\n"
        if(bindings.cbMinus.isChecked)
            result+= "The subtraction is " +
                    "${bindings.etFirstValue.toInt() - bindings.etSecondValue.toInt()}\n"
        if(!bindings.cbPlus.isChecked && !bindings.cbMinus.isChecked)
            result += "Select an operation."
        bindings.tvResult.text = result

    }
}