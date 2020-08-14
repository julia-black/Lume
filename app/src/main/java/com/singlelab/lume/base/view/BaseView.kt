package com.singlelab.lume.base.view

import com.singlelab.lume.model.profile.PersonNotifications
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface BaseView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toAuth()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showNotifications(notifications: PersonNotifications)
}