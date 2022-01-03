package es.usj.mastertsa.jchueca.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastertsa.jchueca.finalproject.databinding.ActivityChallengesBinding
import es.usj.mastertsa.jchueca.finalproject.fragments.ChallengesFragment
import es.usj.mastertsa.jchueca.finalproject.model.Challenge
import android.R
import es.usj.mastertsa.jchueca.finalproject.databinding.ActivityMainBinding


class ChallengesActivity : AppCompatActivity()  {

    private lateinit var bindings: ActivityChallengesBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityChallengesBinding.inflate(layoutInflater)
        setContentView(bindings.root)
        if (savedInstanceState != null) {
            return
        }
        supportActionBar!!.hide()

        initChallenges()

        bindings.btnBack.setOnClickListener {
            finish()
        }

        val challengesFragment = ChallengesFragment()
        challengesFragment.arguments = intent.extras
    }

    override fun onResume() {
        super.onResume()
        // Update points
        val totalPoints = SaveLoad.load(this).toString()
        "Total points:\n$totalPoints".also { bindings.tvChallengesPoints.text = it }
    }

    private fun initChallenges() {

        for (i in 0..2) {
            var challenge = SaveLoad.loadChallenge(this, i)
            if(challenge == null){
                challenge = Challenge(i)
            }
            initChallengeFragment(challenge)
        }


    }

    private fun initChallengeFragment(challenge: Challenge) {
        val challengeFrag = supportFragmentManager.
            findFragmentByTag("frgChallenge${challenge.id}") as ChallengesFragment?
        challengeFrag!!.updateChallengeView(challenge)
    }
}