package com.singlelab.lume.ui.event

import com.singlelab.data.model.event.Event
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface EventView : LoadingView, ErrorView {

    fun showEvent(it: Event)
}