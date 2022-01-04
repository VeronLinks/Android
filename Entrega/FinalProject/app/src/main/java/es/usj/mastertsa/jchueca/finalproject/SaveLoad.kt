package es.usj.mastertsa.jchueca.finalproject

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import es.usj.mastertsa.jchueca.finalproject.model.Challenge

object SaveLoad {

    private const val FILE_NAME = "ExerciseGameApp"

    public fun save (activity : AppCompatActivity, points : Int) {

        val sp: SharedPreferences = activity.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )

        val editor = sp.edit()
        editor.putString(
            "points",
            points.toString()
        )

        editor.apply()
    }

    public fun load (activity : AppCompatActivity) : Int {

        val sp: SharedPreferences = activity.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )

        val pointsAmount = sp.getString("points", "0")

        return pointsAmount!!.toInt()

    }

    public fun saveChallenge(activity : AppCompatActivity, challenge: Challenge) {

        val sp: SharedPreferences = activity.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )

        val editor = sp.edit()
        val challengeStr = "challenge${challenge.id}"
        editor.putString(
            "${challengeStr}_description",
            challenge.description
        )
        editor.putString(
            "${challengeStr}_points",
            challenge.points.toString()
        )
        editor.putString(
            "${challengeStr}_isCompleted",
            challenge.isCompleted.toString()
        )
        editor.putString(
            "${challengeStr}_latitude",
            challenge.latitude.toString()
        )
        editor.putString(
            "${challengeStr}_longitude",
            challenge.longitude.toString()
        )

        editor.apply()
    }


    public fun loadChallenge(activity: AppCompatActivity, id: Int): Challenge? {

        val sp: SharedPreferences = activity.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )

        val challengeStr = "challenge${id}"
        val description = sp.getString("${challengeStr}_description", "")!!
        if(description == ""){
            return null
        }
        val points      = sp.getString("${challengeStr}_points", "0")!!.toInt()
        val isCompleted = sp.getString("${challengeStr}_isCompleted", "false")!!.toBoolean()
        val latitude    = sp.getString("${challengeStr}_latitude", "0")!!.toDouble()
        val longitude   = sp.getString("${challengeStr}_longitude", "0")!!.toDouble()

        return Challenge(
            id,
            description,
            points,
            isCompleted,
            latitude,
            longitude
        )
    }

}