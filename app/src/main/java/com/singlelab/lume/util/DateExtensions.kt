package com.singlelab.lume.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.singlelab.lume.model.Const
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*

fun String.parse(inputDateFormat: String, outputDateFormat: String): String {
    val inputFormat = SimpleDateFormat(inputDateFormat, Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone(Const.UTC)
    return inputFormat.parse(this).parseToString(outputDateFormat)
}

fun Date?.parseToString(outputDateFormat: String): String {
    val outputFormat = SimpleDateFormat(outputDateFormat, Locale.getDefault())
    return if (this != null) outputFormat.format(this) else ""
}

fun Calendar.parseToString(outputDateFormat: String): String {
    val date = Date(this.timeInMillis)
    return date.parseToString(outputDateFormat)
}

fun Date?.formatToUTC(outputDateFormat: String): String {
    val dateFormat = SimpleDateFormat(outputDateFormat, Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone(Const.UTC)
    return if (this != null) dateFormat.format(this) else ""
}

fun List<String>.toCalendarDays(inputDateFormat: String): List<CalendarDay> {
    return this.map {
        val isoDate = it.parse(inputDateFormat, Const.DATE_FORMAT_ISO)
        CalendarDay.from(LocalDate.parse(isoDate))
    }
}