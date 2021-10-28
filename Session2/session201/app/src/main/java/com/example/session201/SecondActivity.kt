package com.example.session201

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.session201.databinding.ActivitySecondBinding

const val DATA_KEY = "data"
class SecondActivity : BaseAppCompatActivity<ActivitySecondBinding>() {

    private val activityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        bindings.tvResult.text = it.data?.getStringExtra(DATA_KEY)
    }

    override val bindings : ActivitySecondBinding by lazy {
        ActivitySecondBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindings.btnSecondActivityToFirst.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        bindings.btnSecondToFourthForResult.setOnClickListener{
            val intent = Intent(this, FourthActivity::class.java)
            activityForResult.launch(intent)
        }
    }
}