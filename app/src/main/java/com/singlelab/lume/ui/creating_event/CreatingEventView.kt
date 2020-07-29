package com.singlelab.lume.ui.creating_event

import android.graphics.Bitmap
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface CreatingEventView : LoadingView, ErrorView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onCompleteCreateEvent(eventUid: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDateStart(dateStr: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDateEnd(dateStr: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun addImage(bitmap: Bitmap)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCurrentCity(cityId: Int?, cityName: String?)
}