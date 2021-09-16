package com.inator.calculator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inator.calculator.R
import com.inator.calculator.adapters.HistoryAdapter
import com.inator.calculator.adapters.HistoryItemClickListener
import com.inator.calculator.model.History
import com.inator.calculator.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import java.util.*


class HistoryFragment : Fragment(), HistoryItemClickListener {
    private var historyItems = ArrayList<History>()
    private var historyAdapter: HistoryAdapter? = null
    private val viewModel: HistoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyRecyclerView.apply {
            setHasFixedSize(false)
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager.apply {
                reverseLayout = false
                stackFromEnd = true
            }
            historyAdapter = HistoryAdapter(context, historyItems, this@HistoryFragment)
            adapter = historyAdapter

        }

        viewModel.allHistory.observe(viewLifecycleOwner) { list ->
            noHistory.isVisible = list.isEmpty()
            historyAdapter?.updateList(list)
        }
    }


    override fun onItemClicked(history: History) {
        viewModel.setClickedExpression(history)
    }
}