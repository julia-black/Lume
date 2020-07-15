package com.singlelab.lume.base.view

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface LoadingView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showLoading(isShow: Boolean)
}