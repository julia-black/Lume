package com.singlelab.lume.ui.person

import com.singlelab.lume.model.profile.Profile
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface PersonView : LoadingView, ErrorView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProfile(profile: Profile)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onAddedToFriends()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onRemovedFromFriends()
}