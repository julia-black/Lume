package com.singlelab.lume.ui.chat.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.group_incoming_message_item.view.*
import kotlinx.android.synthetic.main.outgoing_message_item.view.*

class PrivateChatMessagesAdapter : ChatMessagesAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ChatMessageItem.Type.INCOMING.code -> PrivateIncomingMessageViewHolder(inflater.inflate(R.layout.private_incoming_message_item, parent, false))
            else -> PrivateOutgoingMessageViewHolder(inflater.inflate(R.layout.outgoing_message_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) =
        holder.bind(messages[position])

    class PrivateIncomingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            itemView.incomingMessageView.text = messageItem.text
        }
    }

    class PrivateOutgoingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            itemView.outgoingMessageView.text = messageItem.text
        }
    }
}