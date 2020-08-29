package com.singlelab.lume.ui.chat.common

import android.view.View
import kotlinx.android.synthetic.main.private_incoming_message_item.view.*

class PrivateChatMessagesAdapter : ChatMessagesAdapter(Type.PRIVATE) {
    class PrivateIncomingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            itemView.incomingMessageView.setMessageText(messageItem.text)
            itemView.incomingMessageImageView.setImage(messageItem)
            itemView.incomingMessageDateView.setDate(messageItem)
        }
    }
}