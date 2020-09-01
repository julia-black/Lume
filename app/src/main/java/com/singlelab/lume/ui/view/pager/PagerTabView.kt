package com.singlelab.lume.ui.view.pager

import android.view.View

interface PagerTabView {
    fun getTitle(): String

    fun getView(): View
}