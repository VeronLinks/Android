package es.usj.mastertsa.jchueca.session101

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import es.usj.mastertsa.jchueca.session101.databinding.ActivityFifthBinding

class FifthActivity : AppCompatActivity() {

    val countryName = arrayOf("Argentina", "Brazil", "Bolivia", "Chile", "Colombia", "Ecuador",
        "Guayana Francesa", "Guyana", "Islas Falkland", "Paraguay", "Peru", "Suriname", "Uruguay",
        "Venezuela")
    val countryPopulation = arrayOf("43132000", "204519000", "10520000", "18006000",
        "45549000", "16279000", "262000", "747000", "3000", "7003000", "31153000", "560000",
        "3310000", "30620000")

    private val bindings: ActivityFifthBinding by lazy {
        ActivityFifthBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.lvCountries.adapter = ArrayAdapter<String>(this,
            R.layout.simple_list_item_1, countryName)
        bindings.lvCountries.onItemClickListener =
            AdapterView.OnItemClickListener {
                    _, _, position, _ -> bindings.tvPopulation.text =
                                            "Population of ${countryName[position]}" + "is ${countryPopulation[position]}" }
    }

}