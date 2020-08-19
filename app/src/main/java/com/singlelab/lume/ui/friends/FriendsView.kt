package com.singlelab.lume.ui.friends

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.lume.model.profile.Person
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface FriendsView : LoadingView, ErrorView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showFriends(friends: MutableList<Person>?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showSearchResult(searchResults: MutableList<Person>, page: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEmptyFriends()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showEmptyPersonsFromContacts()
}