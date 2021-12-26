package es.usj.mastertsa.jchueca.session101

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastertsa.jchueca.session101.databinding.ActivityEighthBBinding

class EighthActivityB : AppCompatActivity() {

    private val bindings: ActivityEighthBBinding by lazy {
        ActivityEighthBBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.btnBack.setOnClickListener { back() }
    }

    private fun back() {
        finish()
    }

}