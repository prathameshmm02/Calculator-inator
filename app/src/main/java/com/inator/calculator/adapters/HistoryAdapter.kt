package com.inator.calculator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.inator.calculator.databinding.ItemHistoryBinding
import com.inator.calculator.model.History

class HistoryAdapter(
    private val context: Context,
    private val historyItems: ArrayList<History>,
    private val listener: HistoryItemClickListener
) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = historyItems[position]
        holder.binding.run {
            expression.text = history.expr
            answer.text = history.answer
            if (shouldCreateHeader(position)) {
                dateHeader.text = history.date
            } else {
                dateHeader.isVisible = false
            }
            if (shouldCreateDivider(position)) {
                divider.isVisible = true
            }
        }

    }

    override fun getItemCount(): Int {
        return historyItems.size
    }

    private fun shouldCreateHeader(position: Int): Boolean {
        //We have to group items by date header
        return if (position == 0) {
            true
        } else {
            val prevHistory = historyItems[position - 1]
            val history = historyItems[position]
            prevHistory.date != history.date
        }
    }

    private fun shouldCreateDivider(position: Int): Boolean {
        return if (position + 1 == historyItems.size) {
            true
        } else {
            val nextHistory = historyItems[position + 1]
            val history = historyItems[position]
            nextHistory.date != history.date
        }
    }

    fun updateList(list: List<History>) {
        historyItems.clear()
        historyItems.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        override fun onClick(v: View?) {
            listener.onItemClicked(historyItems[adapterPosition])
        }

        init {
            binding.run {
                root.setOnClickListener(this@ViewHolder)
                answer.setOnClickListener(this@ViewHolder)
                expression.setOnClickListener(this@ViewHolder)
            }
        }
    }
}

interface HistoryItemClickListener {
    fun onItemClicked(history: History)
}