package com.singlelab.lume.ui.auth

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType


interface AuthView : LoadingView, ErrorView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onCodeSend(phone: String, isPushCode: Boolean = false)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toProfile()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toRegistration()
}