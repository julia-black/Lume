package com.singlelab.lume.ui.chats

import com.singlelab.data.model.chats.ChatsInfoItem
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChatsView : LoadingView, ErrorView {
    fun showChats(chats: List<ChatsInfoItem>)
}