package es.usj.mastertsa.jveron.session201

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item!!.itemId
        val option : String
        when(id) {
            R.id.miFirstOption ->
                option = resources.getString(R.string.first_option)
            R.id.miSecondOption ->
                option = resources.getString(R.string.second_option)
            else ->
                option = resources.getString(R.string.third_option)
        }
        Toast.makeText(this, option, Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }

}