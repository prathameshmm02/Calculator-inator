package com.inator.calculator.helper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class SwipeItemTouchHelper(adapter: SwipeHelperAdapter) :
    ItemTouchHelper.Callback() {
    var bgColorCode: Int = Color.rgb(0, 90, 197)
    private val mAdapter: SwipeHelperAdapter
    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        // Set movement flags based on the layout manager
        return if (recyclerView.layoutManager is GridLayoutManager) {
            val dragFlags =
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            makeMovementFlags(dragFlags, swipeFlags)
        } else {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return source.itemViewType == target.itemViewType
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        // Notify the adapter of the dismissal
        mAdapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView: View = viewHolder.itemView
            val background: Drawable = ColorDrawable()
            (background as ColorDrawable).color = bgColorCode
            if (dX > 0) { // swipe right
                background.setBounds(
                    itemView.left,
                    itemView.top,
                    dX.toInt(),
                    itemView.bottom
                )
            } else { // swipe left
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
            }
            background.draw(c)
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is TouchViewHolder) {
                // Let the view holder know that this item is being moved or dragged
                val itemViewHolder: TouchViewHolder? =
                    viewHolder
                itemViewHolder!!.onItemSelected()
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.alpha = ALPHA_FULL
        if (viewHolder is TouchViewHolder) {
            // Tell the view holder it's time to restore the idle state
            val itemViewHolder: TouchViewHolder =
                viewHolder
            itemViewHolder.onItemClear()
        }
    }

    interface SwipeHelperAdapter {
        fun onItemDismiss(position: Int)
    }

    interface TouchViewHolder {
        fun onItemSelected()
        fun onItemClear()
    }

    companion object {
        const val ALPHA_FULL = 1.0f
    }

    init {
        mAdapter = adapter
    }
}