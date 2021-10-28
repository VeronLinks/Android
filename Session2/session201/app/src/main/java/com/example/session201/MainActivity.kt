package com.example.session201

import android.content.Intent
import android.os.Bundle
import com.example.session201.databinding.ActivityMainBinding

class MainActivity : BaseAppCompatActivity<ActivityMainBinding>() {

    override val bindings : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindings.btnFirstActivityToSecond.setOnClickListener{
            val intent = Intent(this, SecondActivity::class.java)
            //val implicit = Intent(Intent.ACTION_CALL)
            //intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        bindings.btnFirstActivityToThird.setOnClickListener{
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }
    }
}