package com.inator.calculator.views


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.appcompat.widget.SearchView
import androidx.core.os.BundleCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.inator.calculator.adapters.SpinnerAdapter
import com.inator.calculator.databinding.ViewSearchableSpinnerBinding
import com.inator.calculator.model.Rate

class SearchableSpinnerDialog : DialogFragment(), SearchView.OnQueryTextListener {

    private var _binding: ViewSearchableSpinnerBinding? = null
    private val binding get() = _binding!!

    private var mDialogTitle: String? = null
    private var mDismissText: String? = null

    lateinit var mClickListener: OnItemClickListener<Rate>

    var listAdapter: Adapter? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = LayoutInflater.from(activity)
        _binding = ViewSearchableSpinnerBinding.inflate(layoutInflater)

        // listView
        binding.listView.apply {
            divider = null
            adapter = listAdapter
            isTextFilterEnabled = true
            setOnItemClickListener { _, _, position, _ ->
                mClickListener.onSearchableItemClicked(
                    listAdapter?.getItem(position),
                    listAdapter?.findOriginalPosition(position) ?: -1
                )
                dismiss()
            }
            // get selected item from spinner and move the lists top position to it
            arguments?.getInt("position")?.let { setSelection(it) }
        }

        // searchView
        binding.search.apply {
            setOnQueryTextListener(this@SearchableSpinnerDialog)
            clearFocus()
        }
        // restore state
        if (savedInstanceState != null) {
            @Suppress("UNCHECKED_CAST")
            mClickListener =
                savedInstanceState.getSerializable("clickListener") as OnItemClickListener<Rate>
            binding.listView.onRestoreInstanceState(savedInstanceState.getParcelable("listView.state"))
        }

        return MaterialAlertDialogBuilder(requireActivity())
            .setView(binding.root)
            .setTitle(mDialogTitle)
            .setNegativeButton(
                if (mDismissText.isNullOrBlank()) getString(android.R.string.cancel) else mDismissText,
                null
            )
            .create()
    }

    override fun onQueryTextChange(query: String?): Boolean {
        listAdapter?.filter?.filter(query)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        binding.search.clearFocus()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("clickListener", mClickListener)
        outState.putParcelable("listView.state", binding.listView.onSaveInstanceState())
        super.onSaveInstanceState(outState)
    }

    // intentionally close dialog on orientation change. else it's a real mess to restore the
    // adapter. really not worth the effort!
    override fun onPause() {
        super.onPause()
        // needs to be done manually, as this fragment/adapter seems to get re-used. the filter won't get reset
        listAdapter?.reset()
        dismiss()
    }

    fun setDismissText(closeText: String?) {
        mDismissText = closeText
    }

    fun setTitle(dialogTitle: String?) {
        mDialogTitle = dialogTitle
    }


    class Adapter(context: Context, private val mAdapter: SpinnerAdapter?) :
        ArrayAdapter<Rate>(context, -1) {

        private var mItems: MutableList<Rate> = mutableListOf()
        private var mItemsFiltered: MutableList<Rate> = mutableListOf()

        init {
            // copy all items of the original adapter to the two local lists, to work with them
            if (mAdapter != null)
                for (i in 0 until mAdapter.count) {
                    val item = mAdapter.getItem(i)
                    mItems.add(item)
                    mItemsFiltered.add(item)
                }
        }

        // find the position of an item in mItems, given the position of an item in mItemsFiltered
        internal fun findOriginalPosition(position: Int): Int {
            if (mAdapter != null)
                for (i in 0 until mAdapter.count) {
                    if (getItem(position) == mAdapter.getItem(i))
                        return i
                }
            return -1
        }

        override fun getCount(): Int {
            return mItemsFiltered.size
        }

        override fun getItem(position: Int): Rate {
            return mItemsFiltered[position]
        }

        // use the dropDownView from the original adapter
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getDropDownView(position, convertView, parent)!!
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
            return mAdapter?.getDropDownView(findOriginalPosition(position), convertView, parent)
        }

        // find all rates based on both their code name or their full name
        override fun getFilter(): Filter {
            return object : Filter() {
                // background thread
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val results = FilterResults()
                    val itemsFiltered = mItems.filter { rate ->
                        if (constraint != null)
                        // full name
                            rate.getName(context)?.contains(constraint, ignoreCase = true) == true
                                    // code name
                                    || rate.code.contains(constraint, ignoreCase = true)
                        else
                            true
                    }
                    results.values = itemsFiltered
                    results.count = itemsFiltered.size
                    return results
                }

                // ui thread
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    @Suppress("UNCHECKED_CAST")
                    if (results?.values != null) {
                        mItemsFiltered = results.values as MutableList<Rate>
                        notifyDataSetChanged()
                    }
                }
            }
        }

        internal fun reset() {
            mItemsFiltered = mItems
        }
    }

}