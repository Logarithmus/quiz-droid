package dev.logarithmus.quizdroid.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import dev.logarithmus.quizdroid.R
import kotlin.system.exitProcess

class FirebaseFailedDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.firebase_failed_msg)
                .setNegativeButton(R.string.exit) { _, _ -> exitProcess(1)}
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}