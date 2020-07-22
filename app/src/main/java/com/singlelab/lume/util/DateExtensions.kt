package com.singlelab.lume.util

import com.singlelab.lume.model.Const
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