package es.usj.mastertsa.jchueca.finalproject

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

object SaveLoad {

    public fun save (activity : AppCompatActivity, points : Int) {

        val sp: SharedPreferences = activity.getSharedPreferences(
            "ExerciseGameApp",
            Context.MODE_PRIVATE
        ) //YourFileName= Any file name you give it

        val editor = sp.edit()
        editor.putString(
            "points",
            points.toString()
        ) //levels=key at which data is stored, your_level_value=No. of levels completed

        editor.apply()
    }

    public fun load (activity : AppCompatActivity) : Int {

        val sp: SharedPreferences = activity.getSharedPreferences(
            "ExerciseGameApp",
            Context.MODE_PRIVATE
        ) //YourFileName= Any file name you give it

        val levelCompleted =
            sp.getString("points", "0") //levels=key at which no. of levels is saved.

        return levelCompleted!!.toInt()

    }

}