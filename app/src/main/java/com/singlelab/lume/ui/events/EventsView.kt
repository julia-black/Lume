package com.singlelab.lume.ui.events

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.lume.model.event.EventSummary
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface EventsView : LoadingView, ErrorView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEvents(events: List<EventSummary>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEventsOnCalendar(
        pastEvents: MutableList<EventSummary>,
        newInviteEvents: MutableList<EventSummary>,
        futureEvents: MutableList<EventSummary>
    )
}