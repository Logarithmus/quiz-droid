package dev.logarithmus.quizdroid.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import dev.logarithmus.quizdroid.R
import kotlin.system.exitProcess

class FinishDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.finish_question)
                .setPositiveButton(R.string.yes) {
                    _, _ -> (context as FinishDialogListener).finishQuiz()
                }
                .setNegativeButton(R.string.no) { _, _ -> }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface FinishDialogListener {
        fun finishQuiz()
    }
}