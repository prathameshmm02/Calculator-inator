package com.inator.calculator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inator.calculator.model.History
import com.inator.calculator.R
import java.util.*

class HistoryAdapter(
    private val context: Context,
    private val historyItems: ArrayList<History>,
    private val listener: HistoryItemClickListener
) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked(historyItems[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = historyItems[position]
        holder.expression.text = history.expr
        holder.answer.text = history.answer
    }

    override fun getItemCount(): Int {
        return historyItems.size
    }

    fun updateList(list: List<History>) {
        historyItems.clear()
        historyItems.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val answer: TextView = itemView.findViewById(R.id.answer)
        val expression: TextView = itemView.findViewById(R.id.expression)
    }
}

interface HistoryItemClickListener {
    fun onItemClicked(history: History)
}