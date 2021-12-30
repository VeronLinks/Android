package es.usj.mastertsa.jchueca.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastertsa.jchueca.finalproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bindings: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindings.root)

        bindings.tvExample.text = SaveLoad.load(this).toString()
    }
}