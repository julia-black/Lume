package com.singlelab.lume.ui.my_profile

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MyProfileView : LoadingView, ErrorView {
    fun showProfile()

    fun navigateToAuth()
}