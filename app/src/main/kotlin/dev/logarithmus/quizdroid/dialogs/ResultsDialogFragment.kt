package dev.logarithmus.quizdroid.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import dev.logarithmus.quizdroid.R
import kotlin.math.roundToInt

class ResultsDialogFragment(correctCount: Int, totalCount: Int): DialogFragment() {
    init {
        arguments = Bundle().apply{
            putInt("correctCount", correctCount)
            putInt("totalCount", totalCount)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val correctCount = arguments?.getInt("correctCount") ?: 0
        val totalCount = arguments?.getInt("totalCount")?.takeIf{ it > 0 } ?: 1
        val percent = (correctCount.toFloat() / totalCount * 100).roundToInt()
        val message = "Correct: ${correctCount}/${totalCount} (${percent}%)"
        return activity?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.results)
                .setMessage(message)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}