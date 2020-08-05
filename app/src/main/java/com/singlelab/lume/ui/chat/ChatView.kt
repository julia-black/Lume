package com.singlelab.lume.ui.chat

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.lume.ui.chat.common.ChatMessageItem
import com.singlelab.lume.ui.chat.common.PrivateChatMessageItem
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChatView : LoadingView, ErrorView {
    fun showChat(messages: List<ChatMessageItem>)
    fun showEmptyChat()
    fun showNewMessage(message: ChatMessageItem)
}