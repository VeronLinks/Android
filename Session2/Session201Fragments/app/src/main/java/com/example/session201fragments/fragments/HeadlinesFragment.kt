package com.example.session201fragments.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import com.example.session201fragments.R
import com.example.session201fragments.model.ArticlesRepository

class HeadlinesFragment : ListFragment() {

    lateinit var mCallback: OnHeadlineSelectedListener

    interface OnHeadlineSelectedListener {
        fun onArticleSelected(position: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = android.R.layout.simple_list_item_activated_1
        val elements = ArticlesRepository.articles.map { it.header }
        listAdapter = ArrayAdapter(requireContext(), layout, elements)
    }

    override fun onStart() {
        super.onStart()
        if (childFragmentManager.findFragmentById(R.id.headlines_fragment) != null) {
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HeadlinesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}