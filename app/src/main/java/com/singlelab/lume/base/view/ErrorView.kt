package com.singlelab.lume.base.view

import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ErrorView : BaseView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showError(message: String, withRetry: Boolean = false, callRetry: () -> Unit = {})
}