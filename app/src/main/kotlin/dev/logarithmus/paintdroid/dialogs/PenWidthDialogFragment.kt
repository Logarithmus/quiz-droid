package dev.logarithmus.paintdroid.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import dev.logarithmus.paintdroid.R
import dev.logarithmus.paintdroid.databinding.PenWidthDialogBinding

class PenWidthDialogFragment: DialogFragment() {
    private lateinit var view: PenWidthDialogBinding
    private lateinit var listener: PenWidthDialogListener
   
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        view = PenWidthDialogBinding.inflate(LayoutInflater.from(context))
        view.seekbar.progress = listener.penWidth.toInt() - 1
        view.text.text = (view.seekbar.progress + 1).toString()
        view.seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                view.text.text = (view.seekbar.progress + 1).toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        return activity?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.pen_width)
                .setView(view.root)
                .setPositiveButton(R.string.ok) { _, _ ->
                    listener.penWidth = (view.seekbar.progress + 1).toFloat()
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as PenWidthDialogListener
    }

    interface PenWidthDialogListener {
        var penWidth: Float
    }
}