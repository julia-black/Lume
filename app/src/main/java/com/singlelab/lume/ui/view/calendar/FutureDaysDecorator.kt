package com.singlelab.lume.ui.view.calendar

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class FutureDaysDecorator(private val currentDay: CalendarDay) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day.isAfter(currentDay) || day == currentDay
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.BLACK))
    }
}