package com.singlelab.lume.ui.creating_event

import android.graphics.Bitmap
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CreatingEventView : LoadingView, ErrorView {
    fun onCompleteCreateEvent(eventUid: String)

    fun showDateStart(dateStr: String)

    fun showDateEnd(dateStr: String)

    fun addImage(bitmap: Bitmap)
}