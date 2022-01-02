package es.usj.mastertsa.jchueca.finalproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import es.usj.mastertsa.jchueca.finalproject.ARG_POSITION
import es.usj.mastertsa.jchueca.finalproject.R


class ChallengesFragment : Fragment() {
    var mCurrentPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION)
        }
        return inflater.inflate(R.layout.fragment_challenges, container, false)
    }

    override fun onStart() {
        super.onStart()
        val args = arguments
        if (args != null) {
            updateArticleView(args.getInt(ARG_POSITION))
        } else if (mCurrentPosition != -1) {
            updateArticleView(mCurrentPosition)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ARG_POSITION, mCurrentPosition)
    }


    fun updateArticleView(position: Int) {
        val article = activity?.findViewById<TextView>(R.id.article)
        article!!.text = Articles.get(position).content
        mCurrentPosition = position
    }
}