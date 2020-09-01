package com.singlelab.lume.ui.chat.common

import android.view.View
import com.singlelab.lume.ui.chat.common.view.PrivateChatIncomingMessageView

class PrivateChatMessagesAdapter : ChatMessagesAdapter(Type.PRIVATE) {
    class PrivateIncomingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            if (messageItem !is PrivateChatMessageItem) return
            if (itemView !is PrivateChatIncomingMessageView) return
            itemView.setContent(messageItem)
        }
    }
}