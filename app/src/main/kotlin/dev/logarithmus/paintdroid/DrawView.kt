package dev.logarithmus.paintdroid

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawView(context: Context?, attrs: AttributeSet?): View(context, attrs) {
    private data class Step(var paint: Paint, var path: Path)

    private val steps: MutableList<Step> = ArrayList()
    private var currentStepIndex: Int = -1
    private val bgColor = (background as ColorDrawable).color
    var penWidth: Float = 3f
    var penColor: Int = Color.BLACK
    var isEraser = false

    private fun createPaint(): Paint {
        return Paint().apply {
            strokeWidth = penWidth
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            this.color = if (isEraser) { bgColor } else { penColor }
        }
    }
    
    fun canUndo(): Boolean = currentStepIndex >= 0

    fun canRedo(): Boolean = currentStepIndex < steps.size - 1

    fun undo() {
        if (canUndo()) {
            currentStepIndex -= 1
        }
        invalidate()
    }

    fun redo() {
        if (canRedo()) {
            currentStepIndex += 1
        }
        invalidate()
    }

    fun clear() {
        steps.clear()
        currentStepIndex = -1
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                repeat(steps.size - currentStepIndex - 1) { steps.removeLast() }
                val path = Path().apply{ moveTo(event.x, event.y) }
                steps.add(Step(createPaint(), path))
                currentStepIndex += 1
            }
            MotionEvent.ACTION_MOVE -> {
                steps.last().path
                    .lineTo(event.x, event.y)
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        steps.take(currentStepIndex + 1).forEach{canvas?.drawPath(it.path, it.paint)}
    }
}
