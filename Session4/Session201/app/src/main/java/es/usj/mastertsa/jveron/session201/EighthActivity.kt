package es.usj.mastertsa.jveron.session201

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastertsa.jveron.session201.databinding.ActivityEighthBinding
import es.usj.mastertsa.jveron.session201.fragments.ARG_POSITION
import es.usj.mastertsa.jveron.session201.fragments.DetailFragment
import es.usj.mastertsa.jveron.session201.fragments.HeadlinesFragment

class EighthActivity : AppCompatActivity(), HeadlinesFragment.OnHeadlineSelectedListener {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eighth)
        if (R.id.fragment_container != null) {
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
                DetailFragment?
        if (articleFrag != null) {
            articleFrag.updateArticleView(position)
        } else {
            val newFragment = DetailFragment()
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