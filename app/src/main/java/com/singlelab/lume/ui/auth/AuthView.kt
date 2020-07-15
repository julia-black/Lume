package com.singlelab.lume.ui.auth

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AuthView : LoadingView, ErrorView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onCodeSend()
}