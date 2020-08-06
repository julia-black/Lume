package com.singlelab.lume.ui.view.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.view_calendar.view.*
import java.util.*

class FullCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var dateClickListener: OnDateSelectedListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_calendar, this, true)
    }

    fun showView(listener: OnDateSelectedListener?, countMonth: Int = 6) {
        this.dateClickListener = listener
        layout.removeAllViews()
        var countViews = 0
        for (i in 0 until countMonth) {
            val calendar = MaterialCalendarView(context)
            val month = TextView(context)
            month.gravity = Gravity.CENTER_HORIZONTAL
            showCalendar(calendar, month, i)
            layout.addView(
                month, countViews++, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
            layout.addView(
                calendar, countViews++, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showCalendar(calendar: MaterialCalendarView, month: TextView, offset: Int = 0) {
        val today = CalendarDay.today()
        calendar.apply {
            addDecorators(
                SelectorDecorator(context),
                PastDaysDecorator(today),
                FutureDaysDecorator(today)
            )
            setWeekDayLabels(context.resources.getStringArray(R.array.custom_weekdays))
            topbarVisible = false
            setOnDateChangedListener(dateClickListener)

            val nextMonthCalendar = getNextMonth(offset)
            val nextYear = nextMonthCalendar[Calendar.YEAR]
            val nextMonth = nextMonthCalendar[Calendar.MONTH]
            val lastDay = nextMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            month.text = "${context.resources.getStringArray(R.array.months)[nextMonth]} $nextYear"
            state().edit()
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .setMinimumDate(CalendarDay.from(nextYear, nextMonth + 1, 1))
                .setMaximumDate(CalendarDay.from(nextYear, nextMonth + 1, lastDay))
                .commit()
        }
    }

    private fun getNextMonth(offset: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar[Calendar.MONTH] = calendar.get(Calendar.MONTH) + offset
        return calendar
    }
}