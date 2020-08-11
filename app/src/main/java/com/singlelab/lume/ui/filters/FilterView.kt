package com.singlelab.lume.ui.filters

import com.singlelab.lume.model.event.Distance
import com.singlelab.lume.model.event.EventType
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface FilterView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDistance(distance: Distance)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCity(cityName: String?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showTypes(selectedTypes: List<EventType>)
}