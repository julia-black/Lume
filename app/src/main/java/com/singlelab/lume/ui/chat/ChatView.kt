package com.singlelab.lume.ui.chat

import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChatView : LoadingView, ErrorView {

    companion object {
        const val  CHAT_TITLE_BUNDLE_KEY = "chatTitle"
    }
}