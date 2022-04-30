package com.inator.calculator.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inator.calculator.R
import com.inator.calculator.adapters.HistoryAdapter
import com.inator.calculator.adapters.HistoryItemClickListener
import com.inator.calculator.databinding.FragmentHistoryBinding
import com.inator.calculator.model.History
import com.inator.calculator.viewmodel.HistoryViewModel


class HistoryFragment : Fragment(R.layout.fragment_history), HistoryItemClickListener {
    private var historyItems = ArrayList<History>()
    private var historyAdapter: HistoryAdapter? = null
    private val viewModel: HistoryViewModel by activityViewModels()

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHistoryBinding.bind(view)
        binding.historyRecyclerView.apply {
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
            binding.noHistory.isVisible = list.isEmpty()
            historyAdapter?.updateList(list)
        }
    }

    override fun onItemClicked(history: History) {
        viewModel.setClickedExpression(history)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}