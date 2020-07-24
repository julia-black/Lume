package com.singlelab.lume.util

import android.view.View

var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) = if (value) visibility = View.VISIBLE else visibility = View.GONE