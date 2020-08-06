package com.singlelab.lume.ui.view.calendar.span

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan

class CircleSpan(
    private val radius: Float = DEFAULT_RADIUS,
    private val style: Paint.Style = Paint.Style.FILL,
    private val color: Int = 0
) : LineBackgroundSpan {

    companion object {
        const val DEFAULT_RADIUS = 3f
    }

    override fun drawBackground(
        canvas: Canvas, paint: Paint,
        left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
        charSequence: CharSequence,
        start: Int, end: Int, lineNum: Int
    ) {
        val circlePaint = Paint(paint)
        val oldColor = circlePaint.color
        if (color != 0) {
            circlePaint.color = color
        }
        circlePaint.style = style
        circlePaint.strokeWidth = 2f
        canvas.drawCircle(
            (right - left) / 2.toFloat(),
            (bottom - top) / 2.toFloat(),
            radius,
            circlePaint
        )
        paint.color = oldColor
    }
}