package com.inator.calculator.adpters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inator.calculator.History.History
import com.inator.calculator.R
import java.util.*

class HistoryAdapter(private val context: Context, private val historyItems: ArrayList<History>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false)
        return ViewHolder(view)
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val answer: TextView = itemView.findViewById(R.id.answer)
        val expression: TextView = itemView.findViewById(R.id.expression)
        override fun onClick(v: View) {
//            CalculatorFragment.draggablePanel?.smoothPanelClose(300)
//            val calculatorFragment =
//                ((context as AppCompatActivity?)!!.supportFragmentManager.findFragmentById(R.id.historyContainer) as CalculatorFragment?)!!
//            calculatorFragment.setInputText(expression.text.toString())
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}