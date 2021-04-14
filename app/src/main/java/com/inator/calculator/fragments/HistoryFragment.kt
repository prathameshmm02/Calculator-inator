package com.inator.calculator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.calculator.inator.R
import com.inator.calculator.History.AppDatabase
import com.inator.calculator.History.History
import com.inator.calculator.History.HistoryDao
import com.inator.calculator.adpters.HistoryAdapter
import kotlinx.android.synthetic.main.fragment_history.*
import java.util.*

class HistoryFragment : Fragment() {
    private var historyItems = ArrayList<History>()
    private var historyAdapter: HistoryAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.apply {

            reverseLayout = true
            stackFromEnd = true
        }
        historyRecyclerView.apply {
            setHasFixedSize(false)
            layoutManager = linearLayoutManager
            history
            historyAdapter = HistoryAdapter(context, historyItems)
            adapter = historyAdapter
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private val history: Unit
        get() {
            Thread {
                val historyDao: HistoryDao =
                    AppDatabase.getDatabase(requireActivity().applicationContext)
                        .historyDao()
                historyItems.addAll(historyDao.getHistory)
                requireActivity().runOnUiThread { historyAdapter!!.notifyDataSetChanged() }
            }.start()
        }
}