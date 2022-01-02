package es.usj.mastertsa.jchueca.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastertsa.jchueca.finalproject.databinding.ActivityChallengesBinding
import es.usj.mastertsa.jchueca.finalproject.fragments.ChallengesFragment
import es.usj.mastertsa.jchueca.finalproject.fragments.MainMenuOptionsFragment

val ARG_POSITION = "position"

class ChallengesActivity : AppCompatActivity(), MainMenuOptionsFragment.OnHeadlineSelectedListener  {

    private lateinit var bindings: ActivityChallengesBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        if (savedInstanceState != null) {
            return
        }
        val firstFragment = MainMenuOptionsFragment()
        firstFragment.arguments = intent.extras
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, firstFragment).commit()
    }


    override fun onArticleSelected(position: Int) {
        val articleFrag = supportFragmentManager.findFragmentById(R.id.fragment_container) as
                ChallengesFragment?
        if (articleFrag != null) {
            articleFrag.updateArticleView(position)
        } else {
            val newFragment = ChallengesFragment()
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