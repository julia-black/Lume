package com.singlelab.lume.util

import android.view.View
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