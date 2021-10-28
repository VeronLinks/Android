package com.example.session201

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.session201.databinding.ActivityFourthBinding
import com.example.session201.databinding.ActivityMainBinding

class FourthActivity : BaseAppCompatActivity<ActivityFourthBinding>() {



    override val bindings : ActivityFourthBinding by lazy {
        ActivityFourthBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindings.btnSend.setOnClickListener{
            intent.putExtra(DATA_KEY, bindings.etResultValue.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}