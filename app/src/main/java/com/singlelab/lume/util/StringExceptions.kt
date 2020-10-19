package com.singlelab.lume.util

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.KeyEvent
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
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

fun String.isValidCardNum(): Boolean {
    val text = replace(" ", "")
    return text.length == Const.CARD_NUM_LENGTH
}

fun EditText.addCardNumMask() {
    var keyDel = 0
    var resultString = ""
    addTextChangedListener(object : TextWatcher {
        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            var flag = true
            val eachBlock =
                text.toString().split(" ".toRegex()).toTypedArray()
            for (i in eachBlock.indices) {
                if (eachBlock[i].length > 4) {
                    flag = false
                }
            }
            if (flag) {
                setOnKeyListener { _, keyCode, _ ->
                    if (keyCode == KeyEvent.KEYCODE_DEL) keyDel = 1
                    false
                }
                if (keyDel == 0) {
                    if ((text.length + 1) % 5 == 0) {
                        if (text.toString().split(" ".toRegex())
                                .toTypedArray().size <= 3
                        ) {
                            setText("$text ")
                            setSelection(text.length)
                        }
                    }
                    resultString = text.toString()
                } else {
                    resultString = text.toString()
                    keyDel = 0
                }
            } else {
                setText(resultString)
            }
        }

        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int,
            after: Int
        ) {
        }

        override fun afterTextChanged(s: Editable) {}
    })

}

fun String?.compareCities(city: String?): Boolean {
    val cityOne = this?.split(Const.CODE_CHAR_SPACE.toChar())
    val cityTwo = city?.split(Const.CODE_CHAR_SPACE.toChar())
    return if (!cityOne.isNullOrEmpty() && !cityTwo.isNullOrEmpty()) {
        cityOne[0] == cityTwo[0]
    } else {
        false
    }
}