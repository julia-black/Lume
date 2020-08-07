package com.singlelab.lume.ui.view.calendar

import android.graphics.Paint
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.singlelab.lume.ui.view.calendar.span.CircleSpan

class CircleDecorator(
    private val color: Int,
    private val style: Paint.Style = Paint.Style.FILL,
    private val textColor: Int? = null,
    private val daysWithEvent: Collection<CalendarDay>
) :
    DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return daysWithEvent.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        textColor?.let {
            view.addSpan(ForegroundColorSpan(textColor))
        }
        view.addSpan(CircleSpan(radius = 50f, style = style, color = color))
    }
}
