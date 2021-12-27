package es.usj.mastertsa.jchueca.session101

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import es.usj.mastertsa.jchueca.session101.databinding.ActivityNinthABinding

const val URL = "url"

class NinthAActivity : AppCompatActivity() {

    private val bindings: ActivityNinthABinding by lazy {
        ActivityNinthABinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.btnVisit.setOnClickListener { visit() }
    }

    private fun visit() {
        if(!bindings.etUrl.text.toString().isNullOrBlank()) {
            val intent = Intent(this, NinthBActivity::class.java)
            intent.putExtra(URL, bindings.etUrl.text.toString())
            startActivity(intent)
        } else
            Toast.makeText(this,
                resources.getString(R.string.insert_a_valid_url),
                Toast.LENGTH_LONG).show()
    }
}