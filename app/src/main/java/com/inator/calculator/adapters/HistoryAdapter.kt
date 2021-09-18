package com.inator.calculator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inator.calculator.R
import com.inator.calculator.model.History
import java.util.*

class HistoryAdapter(
    private val context: Context,
    private val historyItems: ArrayList<History>,
    private val listener: HistoryItemClickListener
) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = historyItems[position]
        holder.expression.text = history.expr
        holder.answer.text = history.answer
        if (shouldCreateHeader(position)) {
            holder.header.text = history.date
        }
        if (shouldCreateDivider(position)) {
            holder.divider.visibility = View.VISIBLE
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val answer: TextView = itemView.findViewById(R.id.answer)
        val expression: TextView = itemView.findViewById(R.id.expression)
        val header: TextView = itemView.findViewById(R.id.date_header)
        val divider: View = itemView.findViewById(R.id.divider)

        override fun onClick(v: View?) {
            listener.onItemClicked(historyItems[adapterPosition])
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}

interface HistoryItemClickListener {
    fun onItemClicked(history: History)
}