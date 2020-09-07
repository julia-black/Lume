package com.singlelab.lume.ui.creating_event

import android.graphics.Bitmap
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.lume.model.event.EventType
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

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showLocationName(locationName: String?)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showWarningOtherCity(currentCity: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showImages(images: MutableList<Bitmap>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showTypes(types: MutableList<EventType>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showLoadingImages(idx: Int, size: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showTitle(title: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDescription(description: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showClosedEvent(checked: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showOnlineEvent(checked: Boolean)
}