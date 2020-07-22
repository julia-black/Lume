package com.singlelab.lume.ui.event

import com.singlelab.lume.model.event.Event
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface EventView : LoadingView, ErrorView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEvent(event: Event)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toMyProfile()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toProfile(personUid: String)
}