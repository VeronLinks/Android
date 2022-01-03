package es.usj.mastertsa.jchueca.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastertsa.jchueca.finalproject.databinding.ActivityChallengesBinding
import es.usj.mastertsa.jchueca.finalproject.fragments.ChallengesFragment
import es.usj.mastertsa.jchueca.finalproject.model.Challenge
import android.R





class ChallengesActivity : AppCompatActivity()  {

    private lateinit var bindings: ActivityChallengesBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        if (savedInstanceState != null) {
            return
        }

        initChallenges()

        bindings.btnBack.setOnClickListener {
            finish()
        }

        val challengesFragment = ChallengesFragment()
        challengesFragment.arguments = intent.extras
    }



    private fun initChallenges() {

        for (i in 0..2) {
            val challenge = SaveLoad.loadChallenge(this, i)
            initChallengeFragment(challenge!!)
        }


    }

    private fun initChallengeFragment(challenge: Challenge) {
        val challengeFrag = supportFragmentManager.
            findFragmentByTag("frgChallenge${challenge.id}") as ChallengesFragment?
        challengeFrag!!.updateChallengeView(challenge)
    }
}