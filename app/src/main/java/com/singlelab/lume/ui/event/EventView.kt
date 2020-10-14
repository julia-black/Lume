package com.singlelab.lume.ui.event

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.lume.model.event.Event
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface EventView : LoadingView, ErrorView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEvent(event: Event)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toProfile(personUid: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onRejectedEvent()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toInviteFriends(eventUid: String, allParticipantIds: List<String>?)
}