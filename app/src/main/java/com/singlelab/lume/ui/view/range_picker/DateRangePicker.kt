package com.singlelab.lume.ui.view.range_picker

import android.view.View
import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import java.util.*

class DateRangePicker(
    private val supportFragmentManager: FragmentManager,
    private val onNegativeClickListener: View.OnClickListener,
    private val onPositiveClickListener: MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>
) {

    private var picker : MaterialDatePicker<Pair<Long, Long>>? = null

    fun show() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val now = Calendar.getInstance()
        now.timeZone =  TimeZone.getTimeZone(Const.UTC)
        builder.setSelection(Pair(now.timeInMillis, now.timeInMillis))
        builder.setTitleText("")
        builder.setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
        builder.setCalendarConstraints(limitRange()?.build())
        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
        picker = builder.build()
        picker?.apply {
            show(supportFragmentManager, picker.toString())
            addOnNegativeButtonClickListener(onNegativeClickListener)
            addOnPositiveButtonClickListener(onPositiveClickListener)
        }
    }

    fun dismiss() {
        picker?.dismiss()
    }

    private fun limitRange(): CalendarConstraints.Builder? {
        val constraintsBuilderRange = CalendarConstraints.Builder()
        val calendarStart = Calendar.getInstance(Const.RUS_LOCALE)
        calendarStart[Calendar.DAY_OF_MONTH] = calendarStart[Calendar.DAY_OF_MONTH] - 1
        val minDate = calendarStart.timeInMillis
        constraintsBuilderRange.setStart(minDate)
        constraintsBuilderRange.setValidator(RangeValidator(minDate))
        return constraintsBuilderRange
    }
}