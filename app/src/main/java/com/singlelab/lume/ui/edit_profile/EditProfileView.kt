package com.singlelab.lume.ui.edit_profile

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.lume.model.profile.NewProfile
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface EditProfileView : LoadingView, ErrorView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProfile(profile: NewProfile)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onCompleteUpdate()
}