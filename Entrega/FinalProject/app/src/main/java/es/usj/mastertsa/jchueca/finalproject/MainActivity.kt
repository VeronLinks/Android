package es.usj.mastertsa.jchueca.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastertsa.jchueca.finalproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bindings: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindings.root)

        bindings.btnChallenges.setOnClickListener {
            val intent = Intent(this@MainActivity,
                ChallengesActivity::class.java
            )
            startActivity(intent)
        }

        bindings.btnViewAllPhotos.setOnClickListener {
            val intent = Intent(this@MainActivity,
                PhotosMenuActivity::class.java
            )
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Update points
        val totalPoints = SaveLoad.load(this).toString()
        "Total points:\n$totalPoints".also { bindings.tvTotalPoints.text = it }
    }
}