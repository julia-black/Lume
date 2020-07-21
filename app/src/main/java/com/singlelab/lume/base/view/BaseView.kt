package com.singlelab.lume.base.view

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface BaseView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toAuth()
}