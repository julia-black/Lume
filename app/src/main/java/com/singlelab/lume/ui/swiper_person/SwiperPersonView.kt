package com.singlelab.lume.ui.swiper_person

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.lume.model.profile.Person
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface SwiperPersonView : LoadingView, ErrorView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showPerson(person: Person)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toAcceptedPerson(person: Person, eventUid: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEmptySwipes()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSuccessReport()
}