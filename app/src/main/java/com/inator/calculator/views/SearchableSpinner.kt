package com.inator.calculator.views

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.util.AttributeSet
import android.widget.SpinnerAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.FragmentActivity
import com.inator.calculator.model.Rate

//Thanks to sal0max for his awesome Searchable Spinner
// Check out his app https://github.com/sal0max/currencies
class SearchableSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : AppCompatSpinner(context, attrs, defStyleAttr), OnItemClickListener<Rate> {

    private val mContext = context
    private var spinnerDialog = SearchableSpinnerDialog()

    private var dialogTitle: String? = null
    private var dialogCloseText: String? = null

    init {
        spinnerDialog.setTitle(dialogTitle)
        spinnerDialog.setDismissText(dialogCloseText)
        spinnerDialog.mClickListener = this
    }

    override fun performClick(): Boolean {
        return when {
            // dialog is already active
            spinnerDialog.isAdded -> true
            // else show dialog, if this spinner is backed by an adapter
            !spinnerDialog.isVisible && adapter != null -> {
                val fm = findActivity(mContext)?.supportFragmentManager
                if (fm != null) {
                    // give currently selected position to dialog
                    spinnerDialog.arguments =
                        Bundle().apply { putInt("position", selectedItemPosition) }
                    spinnerDialog.show(fm, null)
                }
                true
            }
            // else do nothing
            else -> super.performClick()
        }
    }


    override fun setAdapter(adapter: SpinnerAdapter?) {
        super.setAdapter(adapter)
        spinnerDialog.listAdapter = SearchableSpinnerDialog.Adapter(
            context,
            adapter as com.inator.calculator.adapters.SpinnerAdapter?
        )
    }

    private fun findActivity(context: Context?): FragmentActivity? {
        return when (context) {
            is FragmentActivity -> context
            is ContextWrapper -> findActivity(context.baseContext)
            else -> null
        }
    }

    override fun onSearchableItemClicked(item: Rate?, position: Int) {
        setSelection(position)
    }

}