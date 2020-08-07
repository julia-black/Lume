package com.singlelab.lume.ui.view.calendar

import android.content.Context
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.singlelab.lume.R

class SelectorDecorator(private val context: Context) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade) {
        context.getDrawable(R.color.transparent)?.let {
            view.setSelectionDrawable(it)
        }
    }
}
