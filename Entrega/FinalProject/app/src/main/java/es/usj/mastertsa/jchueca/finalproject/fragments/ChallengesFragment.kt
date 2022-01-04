package es.usj.mastertsa.jchueca.finalproject.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import es.usj.mastertsa.jchueca.finalproject.*
import es.usj.mastertsa.jchueca.finalproject.model.Challenge





class ChallengesFragment : Fragment() {

    var initializedChallenge : Challenge? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var isCompleted: Boolean = false
    var challengeId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenges, container, false)
    }

    public fun initializeChallenge(challenge: Challenge){
        initializedChallenge = challenge
        updateChallengeView()
    }

    private fun updateChallengeView(){

        if (initializedChallenge != null){

            val challenge : Challenge = initializedChallenge!!

            val tag = "frgChallenge${challenge.id}"
            val tagFragment: Fragment? = activity?.supportFragmentManager?.findFragmentByTag(tag)
            val bind = tagFragment?.requireView()

            bind?.findViewById<Button>(R.id.btnGoToMap)?.setOnClickListener { goToMapOrClaim() }
            bind?.findViewById<Button>(R.id.btnCancel)?.setOnClickListener { cancel() }

            latitude = challenge.latitude
            longitude = challenge.longitude
            isCompleted = challenge.isCompleted
            challengeId = challenge.id

            bind?.findViewById<TextView>(R.id.tvChallengeId)?.text = "Challenge ${challenge.id+1}"
            bind?.findViewById<TextView>(R.id.tvDescription)?.text = "${challenge.description}"
            bind?.findViewById<TextView>(R.id.tvChallengesPoints)?.text = "${challenge.points} pts"

            if(isCompleted) {
                bind?.findViewById<Button>(R.id.btnGoToMap)?.text = getString(R.string.claim_reward)
                bind?.findViewById<Button>(R.id.btnCancel)?.isEnabled = false
                bind?.findViewById<TextView>(R.id.tvDescription)?.text = getString(R.string.congratulations)
            }else{
                bind?.findViewById<Button>(R.id.btnCancel)?.isEnabled = true
            }

            if (latitude == 0.0 && longitude == 0.0) {
                bind?.findViewById<Button>(R.id.btnGoToMap)?.text = getString(R.string.accept)
            }
            else {
                bind?.findViewById<Button>(R.id.btnGoToMap)?.text = getString(R.string.go_to_map)
            }

            SaveLoad.saveChallenge(activity as AppCompatActivity, challenge)
        }
    }

    private fun goToMapOrClaim () {
        if(isCompleted){ // Claim reward
            val intent = Intent( activity, ClaimRewardActivity::class.java )
            intent.putExtra("challengeId", challengeId)
            startActivity(intent)
        }else{ // Go to map

            val intent = Intent( activity, MapActivity::class.java )
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("challengeId", challengeId)
            startActivity(intent)
        }
    }

    private fun cancel () {

        val challenge = Challenge(challengeId)
        SaveLoad.saveChallenge(activity as AppCompatActivity, challenge)
        initializeChallenge(challenge)
    }
}