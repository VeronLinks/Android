package com.example.session201fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.example.session201fragments.fragments.ARG_POSITION
import com.example.session201fragments.fragments.DetailsFragment
import com.example.session201fragments.fragments.HeadlinesFragment

class MainActivity : AppCompatActivity(), HeadlinesFragment.OnHeadlineSelectedListener {

    private var fragment_container : FrameLayout? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragment_container = findViewById<FrameLayout>(R.id.fragment_container)
        if (fragment_container != null) {
            if (savedInstanceState != null) {
                return
            }
            val firstFragment = HeadlinesFragment()
            firstFragment.arguments = intent.extras
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit()
        }
    }

    override fun onArticleSelected(position: Int) {
        val articleFrag = supportFragmentManager.findFragmentById(R.id.detail_fragment) as
                DetailsFragment?
        if (articleFrag != null) {
            articleFrag.updateArticleView(position)
        } else {
            val newFragment = DetailsFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            newFragment.arguments = args
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}