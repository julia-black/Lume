package com.singlelab.lume.ui.my_profile

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MyProfileView : MvpView {
    fun showProfile()

    fun navigateToAuth()
}