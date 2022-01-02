package es.usj.mastertsa.jchueca.finalproject.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.ListFragment
import es.usj.mastertsa.jchueca.finalproject.R


class MainMenuOptionsFragment : ListFragment() {
    lateinit var mCallback: OnHeadlineSelectedListener

    interface OnHeadlineSelectedListener {
        fun onArticleSelected(position: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = android.R.layout.simple_list_item_activated_1
    }

    override fun onStart() {
        super.onStart()
        if (fragmentManager?.findFragmentById(R.id.headlines_fragment) != null) {
            listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        }
    }

    override fun onAttach(activity: Context) {
        super.onAttach(activity)
        try {
            mCallback = activity as OnHeadlineSelectedListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnHeadlineSelectedListener")
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        mCallback.onArticleSelected(position)
        listView.setItemChecked(position, true)
    }
}