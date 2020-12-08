package com.singlelab.lume.util

import android.content.res.Resources
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout


fun View.setMargin(
    top: Float? = null,
    right: Float? = null,
    bottom: Float? = null,
    left: Float? = null
) {
    if (layoutParams is ConstraintLayout.LayoutParams) {
        val newLayoutParams = layoutParams as ConstraintLayout.LayoutParams
        top?.let {
            newLayoutParams.topMargin = top.toInt()
        }
        right?.let {
            newLayoutParams.rightMargin = right.toInt()
        }
        bottom?.let {
            newLayoutParams.bottomMargin = bottom.toInt()
        }
        left?.let {
            newLayoutParams.leftMargin = left.toInt()
        }
        layoutParams = newLayoutParams
    }
}

fun Int.dpToPx(): Float {
    return this * Resources.getSystem().displayMetrics.density
}

fun Int.pxToDp(): Float {
    return this / Resources.getSystem().displayMetrics.density
}

fun TextView.handleUrlClicks(onClicked: ((String) -> Unit)? = null) {
    text = SpannableStringBuilder.valueOf(text).apply {
        getSpans(0, length, URLSpan::class.java).forEach {
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        onClicked?.invoke(it.url)
                    }
                },
                getSpanStart(it),
                getSpanEnd(it),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            removeSpan(it)
        }
    }
    movementMethod = LinkMovementMethod.getInstance()
}