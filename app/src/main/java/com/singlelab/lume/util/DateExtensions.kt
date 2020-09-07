package com.singlelab.lume.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.singlelab.lume.model.Const
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*

fun String.parse(inputDateFormat: String, outputDateFormat: String): String {
    val inputFormat = SimpleDateFormat(inputDateFormat, Const.RUS_LOCALE)
    inputFormat.timeZone = TimeZone.getTimeZone(Const.UTC)
    return inputFormat.parse(this).parseToString(outputDateFormat)
}

fun Date?.parseToString(outputDateFormat: String): String {
    val outputFormat = SimpleDateFormat(outputDateFormat, Const.RUS_LOCALE)
    return if (this != null) outputFormat.format(this) else ""
}

fun Calendar.parseToString(outputDateFormat: String): String {
    val date = Date(this.timeInMillis)
    return date.parseToString(outputDateFormat)
}

fun Calendar.getFirstAndLastDayOfWeek(): Pair<CalendarDay, CalendarDay> {
    this[Calendar.DAY_OF_WEEK] = this.firstDayOfWeek
    val dayFirst = this[Calendar.DAY_OF_MONTH]
    val monthFirst = this[Calendar.MONTH] + 1
    val yearFirst = this[Calendar.YEAR]

    if (this.firstDayOfWeek == 2) {
        this[Calendar.DAY_OF_WEEK] = 1
    } else {
        this[Calendar.DAY_OF_WEEK] = 7
    }
    val dayLast = this[Calendar.DAY_OF_MONTH]
    val monthLast = this[Calendar.MONTH] + 1
    val yearLast = this[Calendar.YEAR]
    return Pair(
        CalendarDay.from(yearFirst, monthFirst, dayFirst),
        CalendarDay.from(yearLast, monthLast, dayLast)
    )
}

fun Date?.formatToUTC(outputDateFormat: String): String {
    val dateFormat = SimpleDateFormat(outputDateFormat, Const.RUS_LOCALE)
    dateFormat.timeZone = TimeZone.getTimeZone(Const.UTC)
    return if (this != null) dateFormat.format(this) else ""
}

fun List<String>.toCalendarDays(inputDateFormat: String): List<CalendarDay> {
    return this.map {
        it.toCalendarDay(inputDateFormat)
    }
}

fun String.toCalendarDay(inputDateFormat: String): CalendarDay {
    val isoDate = this.parse(inputDateFormat, Const.DATE_FORMAT_ISO)
    return CalendarDay.from(LocalDate.parse(isoDate))
}

fun CalendarDay.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(this.year, this.month - 1, this.day)
    return calendar
}

fun Long.toDateFormat(format: String): String {
    val date = Date(this)
    return date.parseToString(format)
}

fun String.toLongTime(format: String): Long {
    val inputFormat = SimpleDateFormat(format, Const.RUS_LOCALE)
    inputFormat.timeZone = TimeZone.getTimeZone(Const.UTC)
    val date = inputFormat.parse(this)
    return date.time
}