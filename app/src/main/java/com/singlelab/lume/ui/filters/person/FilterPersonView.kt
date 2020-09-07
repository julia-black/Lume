package com.singlelab.lume.ui.filters.person

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface FilterPersonView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCity(cityName: String?)

}