package com.singlelab.lume.ui.chat

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.lume.ui.chat.common.ChatMessageItem
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChatView : LoadingView, ErrorView {
    fun showChat(messages: List<ChatMessageItem>, page: Int)
    fun showEmptyChat()
    fun showNewMessage(message: ChatMessageItem)
    fun enableMessageSending(isEnabled: Boolean)
    fun showMute(isMuted: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToEvent(eventUid: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToPerson(personUid: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMuteDialog(isMuted: Boolean)
}