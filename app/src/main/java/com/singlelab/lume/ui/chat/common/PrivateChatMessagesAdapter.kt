package com.singlelab.lume.ui.chat.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.group_incoming_message_item.view.*
import kotlinx.android.synthetic.main.outgoing_message_item.view.*
import kotlinx.android.synthetic.main.private_incoming_message_item.view.*
import kotlinx.android.synthetic.main.private_incoming_message_item.view.incomingMessageImageView
import kotlinx.android.synthetic.main.private_incoming_message_item.view.incomingMessageView

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

            val hasImages = messageItem.images.any { it.isNotEmpty() }
            itemView.incomingMessageImageView.isVisible = hasImages
            if (hasImages) {
                val url = if (messageItem.uid == "0") {
                    messageItem.images.first()
                } else {
                    messageItem.images.first().url
                }
                // TODO: Сделать отображение нескольких фото (view c +)
                val imagesCount = messageItem.images.size
                Glide.with(itemView)
                    .setDefaultRequestOptions(RequestOptions().timeout(30_000))
                    .load(url)
                    .into(itemView.incomingMessageImageView)
            }
        }
    }

    class PrivateOutgoingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            itemView.outgoingMessageView.text = messageItem.text

            val hasImages = messageItem.images.any { it.isNotEmpty() }
            itemView.outgoingMessageImageView.isVisible = hasImages
            if (hasImages) {
                val url = if (messageItem.uid == "0") {
                    messageItem.images.first()
                } else {
                    messageItem.images.first().url
                }
                // TODO: Сделать отображение нескольких фото (view c +)
                val imagesCount = messageItem.images.size
                Glide.with(itemView)
                    .setDefaultRequestOptions(RequestOptions().timeout(30_000))
                    .load(url)
                    .into(itemView.outgoingMessageImageView)
            }
        }
    }
}