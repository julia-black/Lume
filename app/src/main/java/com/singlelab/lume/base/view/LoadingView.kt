package com.singlelab.lume.base.view

import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface LoadingView : BaseView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showLoading(isShow: Boolean)
}