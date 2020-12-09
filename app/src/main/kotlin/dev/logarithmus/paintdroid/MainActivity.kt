package dev.logarithmus.paintdroid

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import dev.logarithmus.paintdroid.databinding.ActivityMainBinding
import dev.logarithmus.paintdroid.dialogs.ClearScreenDialogFragment
import dev.logarithmus.paintdroid.dialogs.PenColorDialogFragment
import dev.logarithmus.paintdroid.dialogs.PenWidthDialogFragment

class MainActivity: AppCompatActivity(), PenWidthDialogFragment.PenWidthDialogListener,
    PenColorDialogFragment.PenColorDialogListener,
    ClearScreenDialogFragment.ClearScreenDialogListener {

    private lateinit var activity: ActivityMainBinding
    private lateinit var amvMenu: Menu
    private lateinit var undoItem: MenuItem
    private lateinit var redoItem: MenuItem
    private lateinit var eraserItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activity.root)
        setSupportActionBar(activity.toolbar.root)
        activity.toolbar.menu.setOnMenuItemClickListener { onOptionsItemSelected(it) }
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        amvMenu = activity.toolbar.menu.menu
        menuInflater.inflate(R.menu.menu_main, amvMenu)
        undoItem = amvMenu.findItem(R.id.undo)
        redoItem = amvMenu.findItem(R.id.redo)
        eraserItem = amvMenu.findItem(R.id.eraser)
        DrawableCompat.setTint(undoItem.icon, Color.GRAY)
        DrawableCompat.setTint(redoItem.icon, Color.GRAY)
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        updateUndoRedo()
        return super.dispatchTouchEvent(event)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.pen_width -> PenWidthDialogFragment()
                .show(supportFragmentManager, "PenWidthDialog")
            R.id.pen_color -> PenColorDialogFragment()
                .show(supportFragmentManager, "PenColorDialog")
            R.id.undo -> {
                activity.drawView.undo()
                updateUndoRedo()
            }
            R.id.redo -> {
                activity.drawView.redo()
                updateUndoRedo()
            }
            R.id.eraser -> {
                activity.drawView.isEraser = !activity.drawView.isEraser
                DrawableCompat.setTint(eraserItem.icon,
                    if (activity.drawView.isEraser) { Color.RED } else { Color.BLACK }
                )
            }
            R.id.clear_screen -> ClearScreenDialogFragment()
                .show(supportFragmentManager, "ClearScreenDialog")
            else -> super.onOptionsItemSelected(item)
        }
        return when (item.itemId) {
            R.id.pen_width, R.id.pen_color, R.id.undo,
            R.id.redo, R.id.eraser, R.id.clear_screen -> true
            else -> false
        }
    }

    override var penWidth: Float
        get() = activity.drawView.penWidth
        set(width) { activity.drawView.penWidth = width }

    override var penColor: Int
        get() = activity.drawView.penColor
        set(color) { activity.drawView.penColor = color }

    override fun clearScreen() {
        activity.drawView.clear()
        updateUndoRedo()
    }
    
    private fun updateUndoRedo() {
        DrawableCompat.setTint(undoItem.icon,
            if (activity.drawView.canUndo()) { Color.BLACK } else { Color.GRAY }
        )
        DrawableCompat.setTint(redoItem.icon,
            if (activity.drawView.canRedo()) { Color.BLACK } else { Color.GRAY }
        )
    }
}