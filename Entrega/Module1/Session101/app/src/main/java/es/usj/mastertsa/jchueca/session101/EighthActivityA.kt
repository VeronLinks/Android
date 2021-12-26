package es.usj.mastertsa.jchueca.session101

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastertsa.jchueca.session101.databinding.ActivityEighthABinding

class EighthActivityA : AppCompatActivity() {

    private val bindings: ActivityEighthABinding by lazy {
        ActivityEighthABinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.btnAboutUs.setOnClickListener { aboutUs() }
    }

    private fun aboutUs() {
        var intent = Intent(this, EighthActivityB::class.java)
        startActivity(intent)
    }

}