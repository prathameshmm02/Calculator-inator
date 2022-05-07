package com.inator.calculator.prefs.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.preference.ListPreferenceDialogFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialListPreferenceDialogFragment : ListPreferenceDialogFragmentCompat() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mWhichButtonClicked = DialogInterface.BUTTON_NEGATIVE
        val builder = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(preference.dialogTitle)
            .setIcon(preference.dialogIcon)
            .setPositiveButton(preference.positiveButtonText, this)
            .setNegativeButton(preference.negativeButtonText, this)
        val contentView = onCreateDialogView(requireContext())
        if (contentView != null) {
            onBindDialogView(contentView)
            builder.setView(contentView)
        } else {
            builder.setMessage(preference.dialogMessage)
        }
        onPrepareDialogBuilder(builder)
        return builder.create()
    }

    /** Which button was clicked.  */
    private var mWhichButtonClicked = 0

    override fun onClick(dialog: DialogInterface, which: Int) {
        mWhichButtonClicked = which
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDialogClosedWasCalledFromOnDismiss = true
        super.onDismiss(dialog)
    }

    private var onDialogClosedWasCalledFromOnDismiss = false

    override fun onDialogClosed(positiveResult: Boolean) {
        if (onDialogClosedWasCalledFromOnDismiss) {
            onDialogClosedWasCalledFromOnDismiss = false
            // this means the positiveResult needs to be calculated from our mWhichButtonClicked
            super.onDialogClosed(mWhichButtonClicked == DialogInterface.BUTTON_POSITIVE)
        } else {
            super.onDialogClosed(positiveResult)
        }
    }

    companion object {
        fun newInstance(key: String?): MaterialListPreferenceDialogFragment {
            val fragment = MaterialListPreferenceDialogFragment()
            fragment.arguments = bundleOf(ARG_KEY to  key)
            return fragment
        }
    }
}