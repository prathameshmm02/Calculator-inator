package com.inator.calculator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.inator.calculator.R
import com.inator.calculator.databinding.ItemHistoryBinding
import com.inator.calculator.fragments.HistoryFragment
import com.inator.calculator.helper.SwipeItemTouchHelper
import com.inator.calculator.model.History
import com.inator.calculator.repository.HistoryRepository
import com.inator.calculator.viewmodel.HistoryViewModel

class HistoryAdapter(
    private val context: Context,
    private val historyItems: ArrayList<History>,
    private val listener: HistoryItemClickListener,
    private val viewModel: HistoryViewModel
) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>(), SwipeItemTouchHelper.SwipeHelperAdapter {
    private var swippableHistoryItemsSwiped: ArrayList<SwippableHistoryItem> = ArrayList()
    private var swippableHistoryItems: ArrayList<SwippableHistoryItem> =
        historyItems.map { SwippableHistoryItem.fromHistory(it) } as ArrayList<SwippableHistoryItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = swippableHistoryItems[position].history
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

            viewMain.visibility =
                if (swippableHistoryItems[position].swiped) View.GONE else View.VISIBLE

        }

    }

    override fun getItemCount(): Int {
        return swippableHistoryItems.size
    }

    private fun shouldCreateHeader(position: Int): Boolean {
        //We have to group items by date header
        return if (position == 0) {
            true
        } else {
            val prevHistory = swippableHistoryItems[position - 1].history
            val history = swippableHistoryItems[position].history
            prevHistory.date != history.date
        }
    }

    private fun shouldCreateDivider(position: Int): Boolean {
        return if (position + 1 == swippableHistoryItems.size) {
            true
        } else {
            val nextHistory = swippableHistoryItems[position + 1].history
            val history = swippableHistoryItems[position].history
            nextHistory.date != history.date
        }
    }

    fun updateList(list: List<History>) {
        swippableHistoryItems.clear()
        swippableHistoryItems.addAll(list.map { SwippableHistoryItem.fromHistory(it) })
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener, SwipeItemTouchHelper.TouchViewHolder {

        override fun onClick(v: View?) {
            if (v == binding.viewUndoSwipe.buttonUndo) {
                swippableHistoryItems[adapterPosition].swiped = false
                swippableHistoryItemsSwiped.remove(swippableHistoryItems[adapterPosition])
                notifyItemChanged(adapterPosition)
            } else {
                listener.onItemClicked(historyItems[adapterPosition])
            }
        }

        init {
            binding.run {
                root.setOnClickListener(this@ViewHolder)
                answer.setOnClickListener(this@ViewHolder)
                expression.setOnClickListener(this@ViewHolder)
                viewUndoSwipe.buttonUndo.setOnClickListener(this@ViewHolder)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.grey))
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    override fun onItemDismiss(position: Int) {
        // handle when double swipe
        if (swippableHistoryItems[position].swiped) {
            viewModel.deleteHistoryById(swippableHistoryItems[position].history.eid)
            swippableHistoryItemsSwiped.remove(swippableHistoryItems[position])
            swippableHistoryItems.removeAt(position)
            notifyItemRemoved(position)
            return
        }
        swippableHistoryItems[position].swiped = true
        swippableHistoryItemsSwiped.add(swippableHistoryItems[position])
        notifyItemChanged(position)
    }
}


interface HistoryItemClickListener {
    fun onItemClicked(history: History)
}

data class SwippableHistoryItem(
    val history: History,
    var swiped: Boolean = false
) {
    companion object {
        fun fromHistory(history: History): SwippableHistoryItem {
            return SwippableHistoryItem(
                history = history
            )
        }
    }
}