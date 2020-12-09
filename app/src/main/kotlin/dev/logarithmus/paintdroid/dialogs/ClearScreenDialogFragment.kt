package dev.logarithmus.paintdroid.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import dev.logarithmus.paintdroid.R

class ClearScreenDialogFragment: DialogFragment() {
    private lateinit var listener: ClearScreenDialogListener
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.clear_screen_question)
                .setPositiveButton(R.string.yes) { _, _ -> listener.clearScreen() }
                .setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as ClearScreenDialogListener
    }

    interface ClearScreenDialogListener {
        fun clearScreen()
    }
}