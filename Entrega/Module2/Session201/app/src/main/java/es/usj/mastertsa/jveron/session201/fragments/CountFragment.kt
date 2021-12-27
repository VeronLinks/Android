package es.usj.mastertsa.jveron.session201.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.usj.mastertsa.jveron.session201.R
import kotlinx.android.synthetic.main.fragment_count.*

class CountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_count, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btnMain.setOnClickListener { count() }
        return super.onViewCreated(view, savedInstanceState)
    }

    private fun count() {
        var text = etMain.text.toString()
        tvMain.text = "${text.length}"
    }
}