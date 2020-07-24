package com.singlelab.lume.ui.chats

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.lume.ui.chats.common.ChatItem
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChatsView : LoadingView, ErrorView {
    fun showChats(chats: List<ChatItem>)
    fun showEmptyChats()
}