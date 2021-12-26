package es.usj.mastertsa.jchueca.session101

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastertsa.jchueca.session101.databinding.ActivitySixthBinding

class SixthActivity : AppCompatActivity() {
    private val bindings: ActivitySixthBinding by lazy {
        ActivitySixthBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.ibPhone.setOnClickListener { call() }
    }

    private fun call() {
        bindings.tvAction.text = "Calling..."
    }
}