package com.singlelab.lume.ui.friends

import com.singlelab.data.model.profile.Person
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface FriendsView : LoadingView, ErrorView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showFriends(friends: List<Person>?)
}