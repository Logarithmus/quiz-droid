package dev.logarithmus.paintdroid.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import dev.logarithmus.paintdroid.R


class PenColorDialogFragment: DialogFragment() {
    private lateinit var listener: PenColorDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            ColorPickerDialogBuilder
                .with(context)
                .setTitle("Choose color")
                .initialColor(listener.penColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setPositiveButton(R.string.ok) { _, color, _ -> listener.penColor = color }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .build()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as PenColorDialogListener
    }

    interface PenColorDialogListener {
        var penColor: Int
    }
}