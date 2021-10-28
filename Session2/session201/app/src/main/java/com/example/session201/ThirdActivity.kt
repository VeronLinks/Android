package com.example.session201

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.session201.databinding.ActivityMainBinding
import com.example.session201.databinding.ActivityThirdBinding

class ThirdActivity : BaseAppCompatActivity<ActivityThirdBinding>() {

    override val bindings : ActivityThirdBinding by lazy {
        ActivityThirdBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindings.btnCall.setOnClickListener{
            val callIntent = Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:123"))
            startActivity(callIntent)
        }

        bindings.btnEmail.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, "jjhernandez@usj.es")
            startActivity(emailIntent)
        }

        bindings.btnToMain.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}