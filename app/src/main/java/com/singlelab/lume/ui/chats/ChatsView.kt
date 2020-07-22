package com.singlelab.lume.ui.chats

import com.singlelab.net.model.chat.ChatInfo
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChatsView : LoadingView, ErrorView {
    fun showChats(chats: List<ChatInfo>)
}