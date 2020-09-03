package com.singlelab.lume.ui.view.calendar.span

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import com.singlelab.lume.util.dpToPx

class RoundedSpan(
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
        val roundPaint = Paint(paint)
        val oldColor = roundPaint.color
        if (color != 0) {
            roundPaint.color = color
        }
        val widthStroke = 4
        roundPaint.style = style
        roundPaint.strokeWidth = widthStroke.toFloat()

        val size = right - left
        canvas.drawRoundRect(
            widthStroke.dpToPx(),
            0 - (size - bottom) + radius + (widthStroke.dpToPx() * 1.5f),
            size.toFloat() - widthStroke.dpToPx(),
            size.toFloat() - radius - (widthStroke.dpToPx() * 1.5f),
            radius,
            radius,
            roundPaint
        )
        paint.color = oldColor
    }
}