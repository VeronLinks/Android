package es.usj.mastertsa.jchueca.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.mastertsa.jchueca.finalproject.databinding.ActivityChallengesBinding
import es.usj.mastertsa.jchueca.finalproject.fragments.ChallengesFragment
import es.usj.mastertsa.jchueca.finalproject.model.Challenge


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

        bindings.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        //Update challenges
        initChallenges()
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
            createChallengesFragments(challenge)
        }

    }

    private fun createChallengesFragments(challenge: Challenge){
        var id = R.id.frgChallenge0
        if (challenge.id == 1){
            id = R.id.frgChallenge1
        }
        else if (challenge.id == 2){
            id = R.id.frgChallenge2
        }

        val challengeFrag = supportFragmentManager.findFragmentById(id) as ChallengesFragment?

        if (challengeFrag == null) {
            val newFragment = ChallengesFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(id, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        challengeFrag!!.initializeChallenge(challenge)
    }
}