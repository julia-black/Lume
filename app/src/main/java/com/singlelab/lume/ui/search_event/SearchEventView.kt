package com.singlelab.lume.ui.search_event

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.lume.model.event.EventSummary
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface SearchEventView : LoadingView, ErrorView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showSearchResults(events: List<EventSummary>?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEmptyQuery()
}