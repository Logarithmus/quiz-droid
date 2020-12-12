package dev.logarithmus.quizdroid.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import dev.logarithmus.quizdroid.R
import kotlin.system.exitProcess

class ExitDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.exit_question)
                .setPositiveButton(R.string.yes) { _, _ -> exitProcess(0) }
                .setNegativeButton(R.string.no) { _, _ -> }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}