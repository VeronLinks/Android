package es.usj.mastertsa.jveron.session201

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import es.usj.mastertsa.jveron.session201.databinding.ActivitySecondBinding
import java.util.*

class SecondActivity : AppCompatActivity() {

    private val bindings: ActivitySecondBinding by lazy {
        ActivitySecondBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)

        bindings.swActionBar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                supportActionBar!!.show()
            else
                supportActionBar!!.hide()
        }

        bindings.tvSystemLanguage.text = Locale.getDefault().displayLanguage
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_buttons_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val option = when (item!!.itemId) {
            R.id.camera -> resources.getString(R.string.camera)
            R.id.phone -> resources.getString(R.string.phone)
            R.id.miFirstOption -> resources.getString(R.string.first_option)
            R.id.miSecondOption -> resources.getString(R.string.second_option)
            else -> resources.getString(R.string.third_option)
        }
        Toast.makeText(this, option, Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }

}