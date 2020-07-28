package com.singlelab.lume.ui.chat.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.util.generateImageLinkForPerson
import kotlinx.android.synthetic.main.group_incoming_message_item.view.*
import kotlinx.android.synthetic.main.group_outgoing_message_item.view.*

class GroupChatMessagesAdapter : ChatMessagesAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ChatMessageItem.Type.INCOMING.code -> GroupIncomingMessageViewHolder(inflater.inflate(R.layout.group_incoming_message_item, parent, false))
            else -> GroupOutgoingMessageViewHolder(inflater.inflate(R.layout.group_outgoing_message_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) =
        holder.bind(messages[position])

    class GroupIncomingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            val groupMessageItem = messageItem as? GroupChatMessageItem ?: return

            itemView.incomingMessageView.text = messageItem.text

            if (groupMessageItem.personPhoto.isNotEmpty()) {
                Glide.with(itemView)
                    .load(groupMessageItem.personPhoto.generateImageLinkForPerson())
                    .into(itemView.incomingMessagePhotoView)
            }
        }
    }

    class GroupOutgoingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            val groupMessageItem = messageItem as? GroupChatMessageItem ?: return

            itemView.outgoingMessageView.text = messageItem.text

            if (groupMessageItem.personPhoto.isNotEmpty()) {
                Glide.with(itemView)
                    .load(groupMessageItem.personPhoto.generateImageLinkForPerson())
                    .into(itemView.outgoingMessagePhotoView)
            }
        }
    }
}