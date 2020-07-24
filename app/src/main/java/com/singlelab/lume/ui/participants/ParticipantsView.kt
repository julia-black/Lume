package com.singlelab.lume.ui.participants

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.lume.model.profile.Person
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ParticipantsView : LoadingView, ErrorView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showParticipants(list: List<Person>)
}