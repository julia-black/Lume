package com.singlelab.lume.ui.swiper_event

import com.singlelab.lume.model.event.Event
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface SwiperEventView : LoadingView, ErrorView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEvent(event: Event)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toAcceptedEvent(isOpenEvent: Boolean, eventUid: String)
}