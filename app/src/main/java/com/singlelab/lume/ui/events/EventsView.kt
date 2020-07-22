package com.singlelab.lume.ui.events

import com.singlelab.lume.model.event.EventSummary
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface EventsView : LoadingView, ErrorView {

    fun showEvents(events: List<EventSummary>)
}