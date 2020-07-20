package com.singlelab.lume.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes

@AnyRes
fun Context.getAttrValue(@AttrRes attr: Int): Int =
    TypedValue()
        .apply { theme.resolveAttribute(attr, this, true) }
        .resourceId
