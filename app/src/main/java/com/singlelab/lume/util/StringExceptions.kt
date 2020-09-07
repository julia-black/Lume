package com.singlelab.lume.util

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.singlelab.lume.R
import com.singlelab.lume.model.target.Target
import com.singlelab.lume.model.target.TargetType

fun String?.maskPhone(): String {
    if (isNullOrEmpty()) {
        return ""
    } else {
        if (this!!.length == 10) {
            val builder = StringBuilder("+7(")
            this.forEachIndexed { idx, it ->
                builder.append(it)
                if (idx == 2) builder.append(") ")
                if (idx == 5 || idx == 7) builder.append("-")
            }
            return builder.toString()
        }
        return this
    }
}

fun String?.toShortPhone(): String {
    if (!this.isNullOrEmpty()) {
        return replace("[-()\\s]".toRegex(), "")
    }
    return ""
}

fun String.removePostalCode(code: String?): String {
    return if (code != null) {
        this.replace(", $code", "")
    } else {
        this
    }
}

fun String.toUpFirstSymbol(): String {
    val first = this[0].toUpperCase()
    return "$first${substring(1, length)}"
}

fun String.parseDeepLink(): Target? {
    val parts = this.split("/")
    val targetType = TargetType.findByTitle(parts[3]) ?: return null
    val targetId = parts[4]
    return Target(targetType, targetId)
}

fun String.generateEventLink(): String {
    return "https://lumemobile.page.link/?link=https://lumemobile.page.link/event/${this}&apn=com.singlelab.lume"
}

fun Spannable.hightlight(context: Context, indexStart: Int, indexEnd: Int): Spannable {
    val spannable = SpannableString(this)
    spannable.setSpan(
        ForegroundColorSpan(
            ContextCompat.getColor(
                context,
                R.color.colorPrimaryAccent
            )
        ),
        indexStart,
        indexEnd,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(
        StyleSpan(Typeface.BOLD),
        indexStart,
        indexEnd,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannable
}