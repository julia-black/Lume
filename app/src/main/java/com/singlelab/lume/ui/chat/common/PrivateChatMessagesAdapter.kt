package com.singlelab.lume.ui.chat.common

import android.view.View
import com.singlelab.lume.ui.chat.common.view.OnClickImageListener
import com.singlelab.lume.ui.chat.common.view.OnClickMessageListener
import com.singlelab.lume.ui.chat.common.view.PrivateChatIncomingMessageView

class PrivateChatMessagesAdapter(listener: OnClickImageListener, messageListener: OnClickMessageListener) :
    ChatMessagesAdapter(Type.PRIVATE, listener = listener, messageListener = messageListener) {
    class PrivateIncomingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem, listener: OnClickImageListener, messageListener: OnClickMessageListener) {
            if (messageItem !is PrivateChatMessageItem) return
            if (itemView !is PrivateChatIncomingMessageView) return
            itemView.setContent(messageItem, listener, messageListener)
        }
    }
}