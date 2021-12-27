package es.usj.mastertsa.jchueca.session101

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import es.usj.mastertsa.jchueca.session101.databinding.ActivityNinthBBinding

class NinthBActivity : AppCompatActivity() {

    private var url: String? = null

    private val bindings: ActivityNinthBBinding by lazy {
        ActivityNinthBBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        url = intent.getStringExtra(URL)
        bindings.wvUrl.webViewClient = WebViewClient()
        bindings.wvUrl.loadUrl(url!!)
    }
}