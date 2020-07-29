package com.singlelab.lume.ui.filters

import com.singlelab.lume.model.event.Distance
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface FilterView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDistance(distance: Distance)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCity(cityName: String)
}