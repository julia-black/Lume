package com.singlelab.lume.ui.swiper_event

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.lume.model.event.Event
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface SwiperEventView : LoadingView, ErrorView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEvent(event: Event)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toAcceptedEvent(isOpenEvent: Boolean, eventUid: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEmptySwipes(isFullFilter: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideFilter()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showInfoDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSuccessReport()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showNewYearImage()
}